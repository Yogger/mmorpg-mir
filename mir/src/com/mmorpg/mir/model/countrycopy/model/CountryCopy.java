package com.mmorpg.mir.model.countrycopy.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.countrycopy.event.CountryCopyEnterEvent;
import com.mmorpg.mir.model.countrycopy.event.CountryCopyFinishEvent;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_Encourage;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_End;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_Start;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_Status;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_Uneroll;
import com.mmorpg.mir.model.countrycopy.packet.SM_Encourage_Change;
import com.mmorpg.mir.model.countrycopy.packet.SM_Get_Encourage_Info;
import com.mmorpg.mir.model.countrycopy.worldmap.CountryCopyWorldMapInstance;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;
import com.windforce.common.utility.collection.ConcurrentHashSet;

public class CountryCopy {
	private CountryId countryId;
	/** 报名的人 */
	private ConcurrentHashSet<Player> enrolledList = new ConcurrentHashSet<Player>();
	/** 助威的人 */
	private ConcurrentHashMap<Integer, Player> encourageList = new ConcurrentHashMap<Integer, Player>();

	/** 战斗进行中 */
	private volatile boolean warring;
	/** 开始副本 */
	private Future<?> startFuture;
	/** 开始副本的时间 */
	private long startTime;
	/** 第一个开始报名的时间 */
	private long enrollStartTime;

	private SpawnManager spawnManager;

	private WorldMapInstance worldMapInstance;

	private Future<?> noticeRefreshFuture;

	/** 所有的可视物 */
	private List<VisibleObject> visibleObjects = new CopyOnWriteArrayList<VisibleObject>();

	public CountryCopy(CountryId countryId, SpawnManager spawnManager) {
		this.countryId = countryId;
		this.spawnManager = spawnManager;
	}

	/**
	 * 助威
	 */
	synchronized public void encourage(Player player) {
		if (startTime == 0L && enrollStartTime == 0L) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_NOT_EXSIT);
		}

		/*if (hasEnrolled(player)) {
			throw new ManagedException(ManagedErrorCode.ENROLL_COUNTRYCOPY_ENCOURAGED);
		}*/

		boolean canReward = player.getCountryCopyInfo().getEncourageCount() < CountryCopyConfig.getInstance().ENCOURAGE_REWARD_COUNT.getValue();

		if (encourageList.size() >= CountryCopyConfig.getInstance().ENCOURAGE_MAX.getValue()) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENCOURAGED_MAX);
		}
		if (!CountryCopyConfig.getInstance().getEncourageRecieveCond().verify(player)) {
			throw new ManagedException(ManagedErrorCode.LEVEL_NOT_ENOUGH);
		}
		if (encourageList.containsValue(player)) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENCOURAGED);
		}

		player.getCountryCopyInfo().addEncourage();
		int left = CountryCopyConfig.getInstance().ENCOURAGE_REWARD_COUNT.getValue() - player.getCountryCopyInfo().getEncourageCount(); 
		Reward reward = Reward.valueOf();
		if (canReward) {
			Map<String, Object> params = New.hashMap();
			params.put("LEVEL", player.getLevel());
			params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
			reward.addReward(RewardManager.getInstance().grantReward(player, CountryCopyConfig.getInstance().ENCOURAGE_REWARD_CHOOSER_GROUP.getValue(),
					ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COUNTRY_COPY_ENCOURAGE_REWARD), params));
		}
		encourageList.put(encourageList.size() + 1, player);
		((CountryCopyWorldMapInstance) worldMapInstance).upgradePlayerEffect();
		PacketSendUtility.sendPacket(player, SM_CountryCopy_Encourage.valueOf(reward, left));
		
		SM_Encourage_Change changeInfo = SM_Encourage_Change.valueOf(this);
		getSelfCountry().sendPackAll(changeInfo);
	}

	/**
	 * 发送助威广播
	 */
	public void sendEncourageChat(Player player, boolean client) {
		/*if (!hasEnrolled(player) && client) {
			throw new ManagedException(ManagedErrorCode.HAVNT_ENROLL_COUNTRYCOPY);
		}*/
		I18nUtils utils = I18nUtils.valueOf(CountryCopyConfig.getInstance().APPLY_ENCOURAGE_NOTICE_I18N.getValue());
		utils.addParm("name", I18nPack.valueOf(player.getName()));
		ChatManager.getInstance().sendSystem(CountryCopyConfig.getInstance().APPLY_ENCOURAGE_NOTICE_CHANNEL.getValue(),
				utils, null, getSelfCountry(), 
				CountryCopyConfig.getInstance().getEncourageRecieveCond());
	}

	public boolean hasEnrolled(Player player) {
		return enrolledList.contains(player);
	}

	/**
	 * 报名
	 * 
	 * @param player
	 */
	synchronized public void enroll(Player player) {
		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		if (warring) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_WARRING);
		}
		if (enrolledList.contains(player)) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENROLLED);
		}
		if (player.getLevel() < CountryCopyConfig.getInstance().ENROLLE_LEVEL.getValue()) {
			throw new ManagedException(ManagedErrorCode.LEVEL_NOT_ENOUGH);
		}
		if (player.getCountryCopyInfo().getLeftCount() <= 0) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENTER_MAXCOUNT);
		}
		enrolledList.add(player);
		
		if (enrolledList.size() == 1) {
			sendEncourageChat(player, false);
			enrollStartTime = System.currentTimeMillis();
			
			for (Player civil : getSelfCountry().getCivils().values()) {
				PacketSendUtility.sendPacket(civil, SM_Get_Encourage_Info.valueOf(0, this, civil));
			}
		}

		if (enrolledList.size() >= CountryCopyConfig.getInstance().ENROLL_MAXSIZE_AUTO_START.getValue()) {
			if (startFuture != null) {
				startFuture.cancel(false);
			}
			start();
		} else if (startFuture == null || startFuture.isCancelled() || startFuture.isDone()) {
			startFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					start();
				}
			}, CountryCopyConfig.getInstance().ENROLLE_START_TIME.getValue() * DateUtils.MILLIS_PER_MINUTE);
		}

	}

	/**
	 * 取消报名
	 * 
	 * @param player
	 */
	synchronized public void unEnrolle(Player player, boolean throwExcpetion) {
		if (warring) {
			if (!throwExcpetion) {
				return;
			}
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_WARRING);
		}
		if (!enrolledList.contains(player)) {
			if (!throwExcpetion) {
				return;
			}
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENROLLED);
		}
		enrolledList.remove(player);
		if (enrolledList.isEmpty() && (startFuture != null || !startFuture.isCancelled())) {
			startFuture.cancel(false);
			encourageList.clear();
			enrollStartTime = 0L;
			
			for (Player civil : getSelfCountry().getCivils().values()) {
				PacketSendUtility.sendPacket(civil, SM_Get_Encourage_Info.valueOf(1, this, civil));
			}
		}
		
		if (!throwExcpetion) {
			PacketSendUtility.sendPacket(player, new SM_CountryCopy_Uneroll());
		}
	}

	synchronized public void start() {
		if (warring) {
			return;
		}
		warring = true;
		int country = countryId.getValue();
		int country1 = country - 1 < 1 ? 3 : country - 1;
		// 生成生物
		Boss boss = (Boss) spawnManager.creatObject(CountryCopyConfig.getInstance().BOSS_SPAW.getValue(),
				countryId.getValue());
		boss.getController().setBroad(false);
		boss.setCountry(MapCountry.valueOf(country1));
		boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {

			@Override
			public void die(Creature creature) {
				end();
			}

		});
		visibleObjects.add(boss);
		spawnManager.bringIntoWorld(boss, countryId.getValue());
		for (String npcId : CountryCopyConfig.getInstance().NPC_SPAWS.getValue()) {
			CountryNpc npc = (CountryNpc) spawnManager.creatObject(npcId, countryId.getValue());
			npc.setCountry(MapCountry.valueOf(country));
			visibleObjects.add(npc);
			spawnManager.bringIntoWorld(npc, countryId.getValue());
		}
		startTime = System.currentTimeMillis();
		enrollStartTime = 0L;
		SM_CountryCopy_Start enrolledStartPack = SM_CountryCopy_Start.valueOf(startTime, true);
		SM_CountryCopy_Start startPack = SM_CountryCopy_Start.valueOf(startTime, false);
		for (Player player : enrolledList) {
			PacketSendUtility.sendPacket(player, enrolledStartPack);
		}

		for (Player player : getSelfCountry().getCivils().values()) {
			if (!enrolledList.contains(player)) {
				PacketSendUtility.sendPacket(player, startPack);
			}
		}
		if (noticeRefreshFuture == null || noticeRefreshFuture.isCancelled()) {
			noticeRefreshFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					SM_CountryCopy_Status sm = createBossPack();
					if (sm != null) {
						sendActMapBossPack(sm);
					}
				}
			}, 10000, 5000);
		}

		I18nUtils utils = I18nUtils.valueOf("302102");
		ChatManager.getInstance()
				.sendSystem(6, utils, null, getSelfCountry(), 
						CountryCopyConfig.getInstance().getNoticeRecieveCond());
		
	}

	synchronized public void end() {
		Iterator<Player> iterator = getWorldMapInstance().playerIterator();
		double rate = 0.0;
		if (encourageList.isEmpty()) {
			rate = 1;
		} else {
			rate = CountryCopyConfig.getInstance().ENCOURAGE_REWARD_ADDITION.getValue()[encourageList.size() - 1];
		}
		
		while (iterator.hasNext()) {
			Player player = iterator.next();
			Map<String, Object> params = New.hashMap();
			params.put("LEVEL", player.getLevel());
			params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
			params.put("ENCOURAGE_REWARD_ADDITION", rate);
			Reward r = RewardManager.getInstance().grantReward(player, CountryCopyConfig.getInstance().FINISH_REWARD_CHOOSER_GROUP.getValue(),
					ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COUNTRY_COPY_END_REWARD), params);
			PacketSendUtility.sendPacket(player, SM_CountryCopy_End.valueOf(r));
			player.getCountryCopyInfo().addFinishedCount();
			EventBusManager.getInstance().submit(CountryCopyFinishEvent.valueOf(player.getObjectId()));
		}
		this.encourageList.clear();
		this.enrolledList.clear();
		this.startTime = 0;
		this.enrollStartTime = 0L;
		if (noticeRefreshFuture != null) {
			noticeRefreshFuture.cancel(false);
		}

		for (VisibleObject vo : visibleObjects) {
			vo.getController().delete();
		}
		visibleObjects.clear();

		warring = false;
		
		for (Player player : getSelfCountry().getCivils().values()) {
			if (getWorldMapInstance().findPlayer(player.getObjectId()) == null) {
				PacketSendUtility.sendPacket(player, SM_Get_Encourage_Info.valueOf(0, this, player));
			}
		}
	}

	synchronized public void enter(Player player) {
		if (!warring) {
			return;
		}
		
		if (player.getLevel() < CountryCopyConfig.getInstance().ENTER_LEVEL.getValue()) {
			throw new ManagedException(ManagedErrorCode.LEVEL_NOT_ENOUGH);
		}
		if ((System.currentTimeMillis() - player.getCountryCopyInfo().getLastEnterTime()) < CountryCopyConfig
				.getInstance().getPlayerEnterCountryCopyCD(player)) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENTER_CD);
		}
		if (player.getCountryCopyInfo().getLeftCount() <= 0) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_ENTER_MAXCOUNT);
		}
		// player.getCountryCopyInfo().addEnter();
		player.getMoveController().stopMoving();
		player.clearUnityBuff();
		
		int mapId = CountryCopyConfig.getInstance().MAPID.getValue();
		long startedTime = CountryCopyConfig.getInstance().STARTED_POISTION_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		Integer[] bornPosition = null;
		if (System.currentTimeMillis() - startTime <= startedTime) {
			bornPosition = CountryCopyConfig.getInstance().BORN_POSITION.getValue();
		} else {
			bornPosition = CountryCopyConfig.getInstance().STARTED_BORN_POSITION.getValue();
		}
		World.getInstance().setPosition(player, mapId, player.getCountryValue(), bornPosition[0], bornPosition[1],
				player.getHeading());
		player.sendUpdatePosition();
		EventBusManager.getInstance().submit(CountryCopyEnterEvent.valueOf(player.getObjectId()));
	}

	synchronized public void quit(Player player) {
		if (player.getMapId() != CountryCopyConfig.getInstance().MAPID.getValue()) {
			return;
		}

		player.getMoveController().stopMoving(); // 停下来
		RouteStep position = player.getCopyHistory().getRouteStep();
		if (position != null) {
			World.getInstance().setPosition(player, position.getMapId(), position.getX(), position.getY(),
					player.getHeading());
		} else {
			// 防止出错让他回新手村
			List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryValue(),
					ConfigValueManager.getInstance().BIRTH_POINT.getValue());
			RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), (byte) 0);
		}
		player.sendUpdatePosition();
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isWarring() {
		return warring;
	}

	public Map<Integer, Player> getEncourageList() {
		return encourageList;
	}

	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	public void setWorldMapInstance(WorldMapInstance worldMapInstance) {
		this.worldMapInstance = worldMapInstance;
	}

	public long getEnrollTime() {
		return enrollStartTime;
	}

	public Npc findCountryCopyBoss() {
		for (VisibleObject visibleObject : visibleObjects) {
			if (visibleObject.getSpawnKey().equals(CountryCopyConfig.getInstance().BOSS_SPAW.getValue())) {
				return (Npc) visibleObject;
			}
		}
		return null;
	}

	public void sendActMapBossPack(Object packet) {
		Iterator<Player> playerIterator = worldMapInstance.playerIterator();
		while (playerIterator.hasNext()) {
			PacketSendUtility.sendPacket(playerIterator.next(), packet);
		}
	}

	public SM_CountryCopy_Status createBossPack() {
		int size = encourageList.size();
		Npc npc = findCountryCopyBoss();
		if (npc == null) {
			return null;
		}
		return SM_CountryCopy_Status.valueOf(size, npc.getLifeStats().getCurrentHp(), npc.getLifeStats().getMaxHp());
	}

	public Country getSelfCountry() {
		return CountryManager.getInstance().getCountries().get(countryId);
	}
}
