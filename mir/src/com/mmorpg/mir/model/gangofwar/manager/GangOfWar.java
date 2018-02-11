package com.mmorpg.mir.model.gangofwar.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.controller.Camps;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarDoorBossController;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarExpNpcController;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarGuardBossController;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarPlayerController;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarSealBossController;
import com.mmorpg.mir.model.gangofwar.controller.Phase;
import com.mmorpg.mir.model.gangofwar.controller.ReliveGodStatusNpcController;
import com.mmorpg.mir.model.gangofwar.controller.ReliveStatusNpcController;
import com.mmorpg.mir.model.gangofwar.event.BecomKingEvent;
import com.mmorpg.mir.model.gangofwar.event.KingAbdicateEvent;
import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_EndList;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_Info;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_RankSimple;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_Status;
import com.mmorpg.mir.model.gangofwar.packet.vo.BossHpVO;
import com.mmorpg.mir.model.gangofwar.packet.vo.BossStatus;
import com.mmorpg.mir.model.gangofwar.packet.vo.DefendGang;
import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarRankItem;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;
import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class GangOfWar {
	private static Logger logger = Logger.getLogger(GangOfWar.class);
	/** 国家 */
	private CountryId countryId;
	private Country country;
	private GangOfWarConfig gangOfWarConfig;
	private SpawnManager spawnManager;
	/** 防守方帮会 */
	private Gang defendGang;
	/** 开战中 */
	private volatile boolean warring;
	/** 战场 */
	private WorldMapInstance worldMapInstance;
	/** 封印boss */
	private Boss sealBoss;
	/** 玩家战场信息 */
	private NonBlockingHashMap<Long, PlayerGangWarInfo> playerMap = new NonBlockingHashMap<Long, PlayerGangWarInfo>();

	/** 结束任务 */
	private Future<?> endFuture;

	/** 防守结束任务 */
	private Future<?> defendEndFuture;

	/** 排名刷新任务 */
	private Future<?> rankFuture;
	/** 剩余时间通报 */
	private List<Future<?>> endBroadFutures = New.arrayList();
	/** 占领封印守卫时间通报 */
	private List<Future<?>> sealBossBroadFutures = New.arrayList();
	/** 剩余时间通报的时间,分钟 */
	private int[] endBroadTimes = new int[] { 15, 20, 25 };
	/** 封印守卫占领通报的时间,分钟 */
	private int[] sealBossBroadTimes = new int[] { 5, 7, 8 };

	/** 所有的可视物 */
	private List<VisibleObject> visibleObjects = new CopyOnWriteArrayList<VisibleObject>();
	/** 阶段 */
	private Phase phase;
	/** 开始时间 */
	private long startTime;
	/** 防守阶段开始时间 */
	private long defendStartTime;

	private boolean mergeFirst;

	public GangOfWar(CountryId country, GangOfWarConfig gangOfWarConfig, SpawnManager spawnManager) {
		this.gangOfWarConfig = gangOfWarConfig;
		this.spawnManager = spawnManager;
		this.countryId = country;
	}

	/**
	 * 开始!
	 */
	synchronized public void start(boolean mergeFirst) {
		if (isWarring()) {
			return;
		}
		// 储君失效
		CountryManager.getInstance().reserveKingEnd(countryId);
		this.country = CountryManager.getInstance().getCountries().get(countryId);
		country.addGangWarCount();
		stopFutures();
		playerMap.clear();
		clearWarObject();
		rankTemp.clear();

		// 构建地图
		worldMapInstance = MapInstanceService.createOrLoadGangOfWarMapCopy(gangOfWarConfig.MAPID.getValue(),
				countryId.getValue());
		long lastWinGangId = CountryManager.getInstance().getCountries().get(countryId).getLastWinGangId();
		if (lastWinGangId == 0 || GangManager.getInstance().get(lastWinGangId) == null) {
			// 攻击阶段
			phase = Phase.ATTACK;
			attackStart();
		} else {
			// 防守阶段
			phase = Phase.DEFEND;
			defendGang = GangManager.getInstance().get(lastWinGangId);
			defendStart();
		}

		// 超时任务
		endFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				end();
			}
		}, gangOfWarConfig.END_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);

		startTime = System.currentTimeMillis();

		// 当前地图所有人自动进入战场
		Iterator<Player> players = worldMapInstance.playerIterator();
		while (players.hasNext()) {
			Player player = players.next();
			playerJoin(player);
			setRightPosition(player);
		}

		for (final Integer time : endBroadTimes) {
			// 排名任务
			Future<?> endBroad = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
				@Override
				public void run() {
					// 通报
					I18nUtils i18nUtils = I18nUtils.valueOf("201003");
					i18nUtils.addParm("n", I18nPack.valueOf((30 - time) + ""));
					ChatManager.getInstance().sendSystem(41008, i18nUtils, null,
							Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()), countryId.getValue());
				}
			}, 0, time * DateUtils.MILLIS_PER_MINUTE);
			endBroadFutures.add(endBroad);
		}

		// 排名任务
		rankFuture = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				rank();
			}
		}, 10000, 5000);

		warring = true;

		for (Gang gang : GangManager.getInstance().getGangs().values()) {
			if (gang.getCountry() == countryId) {
				I18nUtils i18nUtils = I18nUtils.valueOf("301003");
				ChatManager.getInstance().sendSystem(2, i18nUtils, null, gang);
			}
		}

		I18nUtils i18nUtils1 = I18nUtils.valueOf("403001");
		ChatManager.getInstance().sendSystem(7100300, i18nUtils1, null, country);

		ServerState.getInstance().setGangOfWarDate(new Date());

		this.mergeFirst = mergeFirst;
	}

	private void stopFutures() {
		if (rankFuture != null && !rankFuture.isCancelled()) {
			rankFuture.cancel(true);
		}
		if (endFuture != null && !endFuture.isCancelled()) {
			endFuture.cancel(false);
		}
		for (Future<?> endBroad : endBroadFutures) {
			if (endBroad != null && !endBroad.isCancelled()) {
				endBroad.cancel(false);
			}
		}
		for (Future<?> sealBroad : sealBossBroadFutures) {
			if (sealBroad != null && !sealBroad.isCancelled()) {
				sealBroad.cancel(false);
			}
		}
		sealBossBroadFutures.clear();
	}

	/**
	 * 构建战场生物
	 */
	private void spawnObject() {
		// 第一道门
		Boss firstDoor = (Boss) spawnManager.creatObject(gangOfWarConfig.FIRST_DOOR_SPAW.getValue(),
				countryId.getValue(), new GangOfWarDoorBossController(null, worldMapInstance));
		firstDoor.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		Boss firstGuard = (Boss) spawnManager.creatObject(gangOfWarConfig.FIRST_GURAD_BOSS_SPAW.getValue(),
				countryId.getValue(), new GangOfWarGuardBossController(firstDoor));
		// firstGuardBoss.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		visibleObjects.add(firstDoor);
		visibleObjects.add(firstGuard);
		spawnManager.bringIntoWorld(firstDoor, countryId.getValue());
		spawnManager.bringIntoWorld(firstGuard, countryId.getValue());

		// 第二道门
		Boss secondDoor = (Boss) spawnManager.creatObject(gangOfWarConfig.SECOND_DOOR_SPAW.getValue(),
				countryId.getValue(), new GangOfWarDoorBossController(null, worldMapInstance));
		secondDoor.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		Boss secondGuard = (Boss) spawnManager.creatObject(gangOfWarConfig.SECOND_GURAD_BOSS_SPAW.getValue(),
				countryId.getValue(), new GangOfWarGuardBossController(secondDoor));
		secondGuard.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		((GangOfWarDoorBossController) firstDoor.getController()).setNextGuardBoss(secondGuard);
		visibleObjects.add(secondDoor);
		visibleObjects.add(secondGuard);
		spawnManager.bringIntoWorld(secondDoor, countryId.getValue());
		spawnManager.bringIntoWorld(secondGuard, countryId.getValue());

		// 第三道门
		Boss thirdDoor = (Boss) spawnManager.creatObject(gangOfWarConfig.THIRD_DOOR_SPAW.getValue(),
				countryId.getValue(), new GangOfWarDoorBossController(null, worldMapInstance));
		thirdDoor.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		Boss thirdGuard = (Boss) spawnManager.creatObject(gangOfWarConfig.THIRD_GURAD_BOSS_SPAW.getValue(),
				countryId.getValue(), new GangOfWarGuardBossController(thirdDoor));
		thirdGuard.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		((GangOfWarDoorBossController) secondDoor.getController()).setNextGuardBoss(thirdGuard);
		visibleObjects.add(thirdDoor);
		visibleObjects.add(thirdGuard);
		spawnManager.bringIntoWorld(thirdDoor, countryId.getValue());
		spawnManager.bringIntoWorld(thirdGuard, countryId.getValue());

		// 封印大将
		sealBoss = (Boss) spawnManager.creatObject(gangOfWarConfig.SEAL_BOSS_SPAW.getValue(), countryId.getValue(),
				new GangOfWarSealBossController(this));
		sealBoss.getEffectController().setAbnormal(EffectId.GOD.getEffectId());
		visibleObjects.add(sealBoss);
		((GangOfWarDoorBossController) thirdDoor.getController()).setNextGuardBoss(sealBoss);
		spawnManager.bringIntoWorld(sealBoss, countryId.getValue());

		// 城门复活NPC
		StatusNpc firstStatusNpc = (StatusNpc) spawnManager.creatObject(
				GangOfWarConfig.getInstance().FIRST_DOOR_RELIVE_SPAW.getValue(), countryId.getValue(),
				new ReliveStatusNpcController());
		((ReliveStatusNpcController) firstStatusNpc.getController()).setDoor(firstDoor);
		((ReliveStatusNpcController) firstStatusNpc.getController()).setGuard(firstGuard);
		((GangOfWarDoorBossController) firstDoor.getController()).setReliveStatusNpc(firstStatusNpc);

		StatusNpc secondStatusNpc = (StatusNpc) spawnManager.creatObject(
				GangOfWarConfig.getInstance().SECOND_DOOR_RELIVE_SPAW.getValue(), countryId.getValue(),
				new ReliveStatusNpcController());
		((ReliveStatusNpcController) secondStatusNpc.getController()).setDoor(secondDoor);
		((ReliveStatusNpcController) secondStatusNpc.getController()).setGuard(secondGuard);
		((GangOfWarDoorBossController) secondDoor.getController()).setReliveStatusNpc(secondStatusNpc);

		StatusNpc thirdStatusNpc = (StatusNpc) spawnManager.creatObject(
				GangOfWarConfig.getInstance().THIRD_DOOR_RELIVE_SPAW.getValue(), countryId.getValue(),
				new ReliveStatusNpcController());
		((ReliveStatusNpcController) thirdStatusNpc.getController()).setDoor(thirdDoor);
		((ReliveStatusNpcController) thirdStatusNpc.getController()).setGuard(thirdGuard);
		((GangOfWarDoorBossController) thirdDoor.getController()).setReliveStatusNpc(thirdStatusNpc);

		visibleObjects.add(firstStatusNpc);
		visibleObjects.add(secondStatusNpc);
		visibleObjects.add(thirdStatusNpc);

		// 复活点的无敌NPC
		StatusNpc attackGodStatusNpc = (StatusNpc) spawnManager.creatObject(
				GangOfWarConfig.getInstance().ATTACK_GOD_RELIVE.getValue(), countryId.getValue(),
				new ReliveGodStatusNpcController());
		((ReliveGodStatusNpcController) attackGodStatusNpc.getController()).setPhase(Phase.ATTACK);
		((ReliveGodStatusNpcController) attackGodStatusNpc.getController()).setGangOfWar(this);

		StatusNpc defendGodStatusNpc = (StatusNpc) spawnManager.creatObject(
				GangOfWarConfig.getInstance().DEFEND_GOD_RELIVE.getValue(), countryId.getValue(),
				new ReliveGodStatusNpcController());
		((ReliveGodStatusNpcController) defendGodStatusNpc.getController()).setPhase(Phase.DEFEND);
		((ReliveGodStatusNpcController) defendGodStatusNpc.getController()).setGangOfWar(this);

		spawnManager.bringIntoWorld(attackGodStatusNpc, countryId.getValue());
		spawnManager.bringIntoWorld(defendGodStatusNpc, countryId.getValue());

		visibleObjects.add(attackGodStatusNpc);
		visibleObjects.add(defendGodStatusNpc);

		// 经验NPC
		for (String spawnId : GangOfWarConfig.getInstance().EXP_SPAWS.getValue()) {
			StatusNpc expStatusNpc = (StatusNpc) spawnManager.creatObject(spawnId, countryId.getValue(),
					new GangOfWarExpNpcController());
			spawnManager.bringIntoWorld(expStatusNpc, countryId.getValue());
			visibleObjects.add(expStatusNpc);
		}

		for (VisibleObject visibleObject : visibleObjects) {
			if (visibleObject instanceof Boss) {
				Boss boss = (Boss) visibleObject;
				BossStatus bs = BossStatus.valueOf(boss.getLifeStats().getMaxHp(), boss.getEffectController()
						.isAbnoramlSet(EffectId.GOD));
				bossHpTemp.put(boss.getSpawnKey(), bs);
			}
		}

	}

	public ArrayList<GangOfWarRankItem> getTopPlayerWarInfo(int start, int end) {
		ArrayList<GangOfWarRankItem> rankInfo = new ArrayList<GangOfWarRankItem>();
		int i = 1;
		for (PlayerGangWarInfo pi : rankTemp) {
			if (i > end) {
				break;
			}
			if (start < i && pi.getTotalKill() != 0) {
				rankInfo.add(pi.createRankInfo());
			}
			i++;
		}
		return rankInfo;
	}

	public int notEmptySize() {
		int i = 0;
		for (PlayerGangWarInfo pi : rankTemp) {
			if (pi.getTotalKill() != 0) {
				i++;
			}
		}
		return i;
	}

	public ArrayList<GangOfWarRankItem> getTopPlayerWarInfo(int max) {
		return getTopPlayerWarInfo(0, max);
	}

	/**
	 * 进攻开始
	 */
	public void attackStart() {
		// 生成怪物
		spawnObject();
		synchronized (playerMap) {
			for (PlayerGangWarInfo pgw : playerMap.values()) {
				if (pgw.getPlayer().getController() instanceof GangOfWarPlayerController) {
					((GangOfWarPlayerController) pgw.getPlayer().getController()).setCamps(Camps.ATTACK);
				}
			}
		}
	}

	/**
	 * 防守开始
	 */
	public void defendStart() {
		// 生成怪物
		spawnObject();
		synchronized (playerMap) {
			for (PlayerGangWarInfo pgw : playerMap.values()) {
				if (pgw.getPlayer().getController() instanceof GangOfWarPlayerController) {
					if (pgw.getPlayer().getGang() == defendGang) {
						((GangOfWarPlayerController) pgw.getPlayer().getController()).setCamps(Camps.DEFEND);
					} else {
						((GangOfWarPlayerController) pgw.getPlayer().getController()).setCamps(Camps.ATTACK);
					}
				}
			}
		}
		defendStartTime = System.currentTimeMillis();

		for (final Integer time : sealBossBroadTimes) {
			Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					// 通报
					I18nUtils i18nUtils = I18nUtils.valueOf("10208");
					i18nUtils.addParm("family", I18nPack.valueOf(defendGang.getName()));
					i18nUtils.addParm("n", I18nPack.valueOf(time + ""));
					ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
							Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()), countryId.getValue());
				}
			}, time * DateUtils.MILLIS_PER_MINUTE);
			sealBossBroadFutures.add(future);
		}

		defendEndFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				end();
			}
		}, GangOfWarConfig.getInstance().DEFEND_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
		phase = Phase.DEFEND;
	}

	public void enterWar(Player player) {
		if (!isWarring()) {
			return;
		}
		if (player.getGang() == null) {
			throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
		}
		if (!player.getPosition().isSpawned()) {
			throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
		}
		if (player.isInCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (!player.getPosition().getMapRegion().getParent().getParent().isCopy()) {
			player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		}
		int mapId = GangOfWarConfig.getInstance().MAPID.getValue();
		if (player.getMapId() == mapId) {
			return;
		}
		setRightPosition(player);
	}

	/** 活动中积分缓存 */
	private List<PlayerGangWarInfo> rankTemp = new ArrayList<PlayerGangWarInfo>();

	/**
	 * 刷新排名
	 */
	public void rank() {
		List<PlayerGangWarInfo> rankTemp = new ArrayList<PlayerGangWarInfo>();
		for (PlayerGangWarInfo pi : playerMap.values()) {
			rankTemp.add(pi);
		}
		Collections.sort(rankTemp);
		int rank = 1;
		for (PlayerGangWarInfo playerWarInfo : rankTemp) {
			if (playerWarInfo.getRank() != rank) {
				playerWarInfo.setRank(rank);
				playerWarInfo.sendUpdate();
			}
			rank++;
		}
		// 缓存起来便于查询
		this.rankTemp = rankTemp;

		// 发送boss血量变化,
		BossHpVO bossHp = BossHpVO.valueOf();
		for (VisibleObject visibleObject : visibleObjects) {
			if (visibleObject instanceof Boss) {
				Boss boss = (Boss) visibleObject;
				BossStatus bs = BossStatus.valueOf(boss.getLifeStats().getCurrentHp(), boss.getEffectController()
						.isAbnoramlSet(EffectId.GOD));
				// BOSS没有变化就不发送
				if (!bossHpTemp.get(boss.getSpawnKey()).equals(bs)) {
					bossHp.add(boss.getSpawnKey(), bs);
					bossHpTemp.put(boss.getSpawnKey(), bs);
				}
			}
		}
		if (!bossHp.getBossHps().isEmpty()) {
			sendPackOnWar(bossHp);
		}

		// 发送前5名的排名
		SM_GangOfWar_RankSimple sm = SM_GangOfWar_RankSimple.valueOf(getTopPlayerWarInfo(5));
		sendPackOnWar(sm);
	}

	private List<Integer> sealBossDefendBroadeds = New.arrayList();

	/** BOSS 血量缓存,检查是否有变化 */
	private Map<String, BossStatus> bossHpTemp = New.hashMap();

	/**
	 * 发送给在地图中，并且在线的玩家
	 * 
	 * @param packet
	 */
	private void sendPackOnWar(Object packet) {
		for (PlayerGangWarInfo info : playerMap.values()) {
			if (info.getPlayer().getController() instanceof GangOfWarPlayerController) {
				if (SessionManager.getInstance().isOnline(info.getPlayer().getObjectId())) {
					PacketSendUtility.sendPacket(info.getPlayer(), packet);
				}
			}
		}
	}

	public SM_GangOfWar_Status getKingOfWarStatus() {
		if (country == null) {
			this.country = CountryManager.getInstance().getCountries().get(countryId);
		}
		Gang dg = null;
		if (country.getKing() != null && country.getKing().getGang() != null) {
			dg = country.getKing().getGang();
		}
		SM_GangOfWar_Status sm = SM_GangOfWar_Status.valueOf(warring, dg, country, getNextStartTime(country));
		return sm;
	}

	public long getNextStartTime(Country country) {
		long now = System.currentTimeMillis();
		// 最多判断8天就跳出 避免死循环
		for (int i = 0; i < 8; i++) {
			Date nextTime = DateUtils.getNextTime(GangOfWarConfig.getInstance().START_TIME_NOT_KING.getValue(),
					new Date(now));
			if (GangOfWarManager.getInstance().canOpenWar(country, nextTime)) {
				return nextTime.getTime();
			}
			now += DateUtils.MILLIS_PER_DAY;
		}

		return 0;
	}

	public void setRightPosition(Player player) {
		ReliveBaseResource reliveResource = PlayerReliveManager.getInstance().getReliveResource(
				worldMapInstance.getParent().getReliveId());
		RelivePosition relivePosition = null;
		if (Phase.DEFEND == phase && player.getGang() == defendGang) {
			relivePosition = reliveResource.getRelivePositions().get("GOW_DEFEND_RELIVE");
		} else {
			relivePosition = reliveResource.getRelivePositions().get("GOW_ATTACK_RELIVE");
		}
		player.getMoveController().stopMoving();
		if (player.getMapId() == relivePosition.getMapId()
				&& player.getPosition().getInstanceId() == player.getCountryValue()) {
			World.getInstance().updatePosition(player, relivePosition.getX(), relivePosition.getY(),
					player.getHeading());
		} else {
			World.getInstance().setPosition(player, relivePosition.getMapId(), player.getCountryValue(),
					relivePosition.getX(), relivePosition.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
	}

	/**
	 * 玩家加入
	 * 
	 * @param player
	 */
	public void playerJoin(Player player) {
		if (!(player.getController() instanceof GangOfWarPlayerController)) {
			player.setController(new GangOfWarPlayerController(this));
			player.getController().setOwner(player);
		}
		synchronized (playerMap) {
			if (!playerMap.containsKey(player.getObjectId())) {
				playerMap.putIfAbsent(player.getObjectId(), PlayerGangWarInfo.valueOf(player));
			}
			if (Phase.ATTACK == phase) {
				((GangOfWarPlayerController) player.getController()).setCamps(Camps.ATTACK);
			} else if (Phase.DEFEND == phase) {
				if (defendGang == player.getGang()) {
					((GangOfWarPlayerController) player.getController()).setCamps(Camps.DEFEND);
				} else {
					((GangOfWarPlayerController) player.getController()).setCamps(Camps.ATTACK);
				}
			}
		}

		// 推送战场信息
		PacketSendUtility.sendPacket(player, createWarInfo(player));
	}

	/**
	 * 构建当前战场信息
	 * 
	 * @param player
	 * @return
	 */
	private SM_GangOfWar_Info createWarInfo(Player player) {
		SM_GangOfWar_Info info = new SM_GangOfWar_Info();
		info.setEndTime(startTime + gangOfWarConfig.END_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
		if (Phase.DEFEND == phase) {
			DefendGang dg = DefendGang.valueOf(defendStartTime + gangOfWarConfig.DEFEND_TIME.getValue()
					* DateUtils.MILLIS_PER_SECOND, defendGang.getId(), defendGang.getName());
			info.setDefendGang(dg);
		}
		info.setPlayerGangWarInfoVO(playerMap.get(player.getObjectId()).createVO());
		info.setCurrentHp(BossHpVO.valueOf());
		info.setMaxHp(BossHpVO.valueOf());
		for (VisibleObject visibleObject : visibleObjects) {
			if (visibleObject instanceof Boss) {
				Boss boss = (Boss) visibleObject;
				boolean god = boss.getEffectController().isAbnoramlSet(EffectId.GOD);
				info.getCurrentHp()
						.add(boss.getSpawnKey(), BossStatus.valueOf(boss.getLifeStats().getCurrentHp(), god));
				info.getMaxHp().add(boss.getSpawnKey(), BossStatus.valueOf(boss.getLifeStats().getMaxHp(), god));
			}
		}
		return info;
	}

	/**
	 * 结束
	 */
	synchronized public void end() {
		if (!warring) {
			return;
		}
		// 战斗结束
		warring = false;

		stopFutures();
		// 清理战场
		clearWarObject();
		Country country = CountryManager.getInstance().getCountries().get(countryId);
		Official officialKing = country.getCourt().getKing();
		PlayerSimpleInfo kingSimple = (officialKing == null ? null : PlayerManager.getInstance()
				.getPlayer(officialKing.getPlayerId()).createSimple());
		if (Phase.ATTACK != phase) {
			Player player = (officialKing == null ? null : PlayerManager.getInstance().getPlayer(
					officialKing.getPlayerId()));
			// 防守完成
			if (country.getCourt().getKing() == null
					|| (country.getCourt().getKing().getPlayerId() != defendGang.getMaster().getPlayerId())) {
				if (country.getCourt().getKing() != null) {
					EventBusManager.getInstance().submit(
							KingAbdicateEvent.valueOf(country.getCourt().getKing().getPlayerId()));
				}
				// 家族换位
				country.getCourt().reset(true);
				player = PlayerManager.getInstance().getPlayer(defendGang.getMaster().getPlayerId());
				kingSimple = player.createSimple();
				country.getCourt().appoint(player, CountryOfficial.KING, 0);
				country.getCourt().setBecomeKingTime(System.currentTimeMillis());
				country.setLastWinGangId(defendGang.getId());
				KingOfWarManager.getInstance().refreshSculptures(country.getId().getValue());
				EventBusManager.getInstance().submit(BecomKingEvent.valueOf(player.getObjectId()));
			}
			country.getCourt().resetControl();
			// 通报
			I18nUtils i18nUtils = I18nUtils.valueOf("403002");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("family", I18nPack.valueOf(defendGang.getName()));
			ChatManager.getInstance().sendSystem(71003, i18nUtils, null, player.getCountry()); // Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
			// 通报聊天
			I18nUtils i18nUtils1 = I18nUtils.valueOf("302001");
			i18nUtils1.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils1.addParm("family", I18nPack.valueOf(defendGang.getName()));
			ChatManager.getInstance().sendSystem(6, i18nUtils1, null, player.getCountry()); // Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),

		}
		List<Player> playerLs = new ArrayList<Player>();
		// 邮件发奖
		for (PlayerGangWarInfo player : playerMap.values()) {
			try {
				Map<String, Object> parms = new HashMap<String, Object>();
				parms.put("LEVEL", player.getPlayer().getLevel());
				parms.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player.getPlayer()));
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player.getPlayer(),
						GangOfWarConfig.getInstance().END_REWARD_CHOOSER.getValue());
				Reward reward = RewardManager.getInstance().creatReward(player.getPlayer(), rewardIds, parms);
				I18nUtils titel18n = I18nUtils.valueOf(GangOfWarConfig.getInstance().END_MAIL_TITLE.getValue());
				I18nUtils contextl18n = I18nUtils.valueOf(GangOfWarConfig.getInstance().END_MAIL_CONTENT.getValue());
				contextl18n.addParm("country", I18nPack.valueOf(player.getPlayer().getCountry().getName()));
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				List<String> countryNpcSender = ChooserManager.getInstance().chooseValueByRequire(player.getPlayer(),
						KingOfWarConfig.getInstance().MAIL_SENDER_NPC_CHOOSER.getValue());
				mail.setNpcId(countryNpcSender.get(0));
				MailManager.getInstance().sendMail(mail, player.getPlayer().getObjectId());
				// send endList
				if (SessionManager.getInstance().isOnline(player.getPlayer().getObjectId())) {
					PacketSendUtility.sendPacket(player.getPlayer(), SM_GangOfWar_EndList.valueOf(
							System.currentTimeMillis(), player, getTopPlayerWarInfo(5), reward, kingSimple));
				}
				if (!player.getPlayer().isKing()
						&& SessionManager.getInstance().isOnline(player.getPlayer().getObjectId())) {
					playerLs.add(player.getPlayer());
				}

				if (country.getGangWarCount() == 1) {
					// 开服活动 王城争霸奖励
					if (country.getKing() != null
							&& country.getKing().getObjectId() == player.getPlayer().getObjectId()) {
						I18nUtils titel18 = I18nUtils
								.valueOf(gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_TITLE.getValue());
						I18nUtils contextl18 = I18nUtils
								.valueOf(gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_CONTENT.getValue());
						Reward kingActvityReward = RewardManager.getInstance().creatReward(player.getPlayer(),
								gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_KING_REWARD.getValue(), null);
						Mail activityMail = Mail.valueOf(titel18, contextl18, null, kingActvityReward);
						MailManager.getInstance().sendMail(activityMail, player.getPlayer().getObjectId());
					} else {
						I18nUtils titel18 = I18nUtils
								.valueOf(gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_TITLE.getValue());
						I18nUtils contextl18 = I18nUtils
								.valueOf(gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_CONTENT.getValue());
						Reward commonActvityReward = RewardManager.getInstance().creatReward(player.getPlayer(),
								gangOfWarConfig.GANG_WAR_OPEN_ACTIVITY_COMMON_REWARD.getValue(), null);
						Mail activityMail = Mail.valueOf(titel18, contextl18, null, commonActvityReward);
						MailManager.getInstance().sendMail(activityMail, player.getPlayer().getObjectId());
					}
				}

				if (mergeFirst) {
					if (country.getKing() != null
							&& country.getKing().getObjectId() == player.getPlayer().getObjectId()) {
						I18nUtils titel18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_WIN_GAND_LEADER_MAIL_TITLE_IL18NID
										.getValue());
						I18nUtils contextl18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_WIN_GAND_LEADER_MAIL_CONTENT_IL18NID
										.getValue());
						Reward kingActvityReward = RewardManager.getInstance().creatReward(player.getPlayer(),
								MergeActiveConfig.getInstance().MERGE_GOW_WIN_GANG_LEADER_REWARDID.getValue(), null);
						Mail activityMail = Mail.valueOf(titel18, contextl18, null, kingActvityReward);
						MailManager.getInstance().sendMail(activityMail, player.getPlayer().getObjectId());
					} else if (country.getKing() != null
							&& country.getKing().getGang().getId() == player.getPlayer().getGang().getId()) {
						I18nUtils titel18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_WIN_GAND_MEMBER_MAIL_TITLE_IL18NID
										.getValue());
						I18nUtils contextl18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_WIN_GAND_MEMBER_MAIL_CONTENT_IL18NID
										.getValue());
						Reward winMemberReward = RewardManager.getInstance().creatReward(player.getPlayer(),
								MergeActiveConfig.getInstance().MERGE_GOW_WIN_GANG_MEMBER_REWARDID.getValue(), null);
						Mail activityMail = Mail.valueOf(titel18, contextl18, null, winMemberReward);
						MailManager.getInstance().sendMail(activityMail, player.getPlayer().getObjectId());
					} else if (country.getKing() != null
							&& country.getKing().getGang().getId() != player.getPlayer().getGang().getId()) {
						I18nUtils titel18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_LOSE_GAND_MEMBER_MAIL_TITLE_IL18NID
										.getValue());
						I18nUtils contextl18 = I18nUtils
								.valueOf(MergeActiveConfig.getInstance().MERGE_GOW_LOSE_GAND_MEMBER_MAIL_CONTENT_IL18NID
										.getValue());
						Reward loseReward = RewardManager.getInstance().creatReward(player.getPlayer(),
								MergeActiveConfig.getInstance().MERGE_GOW_LOSE_GANG_MEMBER_REWARDID.getValue(), null);
						Mail activityMail = Mail.valueOf(titel18, contextl18, null, loseReward);
						MailManager.getInstance().sendMail(activityMail, player.getPlayer().getObjectId());
					}
				}
			} catch (Exception e) {
				logger.error("GangOfWar end Error", e);
			}
		}

		// 首次家族战自动任命官员
		if (country.getGangWarCount() == 1) {
			Collections.sort(playerLs, new Comparator<Player>() {

				@Override
				public int compare(Player o1, Player o2) {
					int battleSore1 = o1.getGameStats().calcBattleScore();
					int battleSore2 = o2.getGameStats().calcBattleScore();
					int level1 = o1.getLevel();
					int level2 = o2.getLevel();
					if (battleSore1 != battleSore2) {
						return battleSore2 - battleSore1;
					} else {
						return level2 - level1;
					}
				}
			});
			country.getCourt().autoAppoint(country, playerLs);
		}

		// 还原玩家状态
		synchronized (playerMap) {
			for (PlayerGangWarInfo playerGangWarInfo : playerMap.values()) {
				if (playerGangWarInfo.getPlayer().getController() instanceof GangOfWarPlayerController) {
					playerGangWarInfo.getPlayer().setController(new PlayerController());
					playerGangWarInfo.getPlayer().getController().setOwner(playerGangWarInfo.getPlayer());
				}
			}
		}
		playerMap.clear();

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rankTemp.clear();
			}
		}, DateUtils.MILLIS_PER_MINUTE * 20);

	}

	/**
	 * 清理场上所有对象
	 */
	public void clearWarObject() {
		if (defendEndFuture != null && !defendEndFuture.isCancelled()) {
			defendEndFuture.cancel(false);
		}
		for (VisibleObject visibleObject : visibleObjects) {
			// 清理老的物件
			visibleObject.getController().delete();
		}
		visibleObjects.clear();
	}

	/**
	 * 下一轮防守阶段
	 */
	public void nextPhase() {
		if (!warring) {
			return;
		}
		sealBossDefendBroadeds.clear();
		GangOfWarSealBossController gsbController = (GangOfWarSealBossController) sealBoss.getController();
		defendGang = gsbController.getFirstDamageGang();
		clearWarObject();
		rankTemp.clear();
		defendStart();
		sendPackOnWar(DefendGang.valueOf(defendStartTime + gangOfWarConfig.DEFEND_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND, defendGang.getId(), defendGang.getName()));
	}

	public List<VisibleObject> getVisibleObjects() {
		return visibleObjects;
	}

	public void setVisibleObjects(List<VisibleObject> visibleObjects) {
		this.visibleObjects = visibleObjects;
	}

	public boolean isWarring() {
		return warring;
	}

	public void setWarring(boolean warring) {
		this.warring = warring;
	}

	public Phase getPhase() {
		return phase;
	}

	public Gang getDefendGang() {
		return defendGang;
	}

	public List<PlayerGangWarInfo> getRankTemp() {
		return rankTemp;
	}

	public long getStartTime() {
		return startTime;
	}

	public NonBlockingHashMap<Long, PlayerGangWarInfo> getPlayerMap() {
		return playerMap;
	}

}
