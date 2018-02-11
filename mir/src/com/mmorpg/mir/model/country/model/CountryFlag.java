package com.mmorpg.mir.model.country.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.country.broadcast.Broadcast;
import com.mmorpg.mir.model.country.controllers.AllianceSpecialNpcController;
import com.mmorpg.mir.model.country.controllers.CountryNpcFlagAndDachenController;
import com.mmorpg.mir.model.country.event.CountryFlagQuestFinishEvent;
import com.mmorpg.mir.model.country.event.ReserveKingFinishFlagEvent;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.packet.SM_AttendFlag_Already;
import com.mmorpg.mir.model.country.packet.SM_CountryFlag_End;
import com.mmorpg.mir.model.country.packet.SM_CountryFlag_Start;
import com.mmorpg.mir.model.country.packet.SM_Country_Flag_Relive_Push;
import com.mmorpg.mir.model.country.packet.SM_Country_UpgradeFlag;
import com.mmorpg.mir.model.country.packet.SM_Country_War_Reward;
import com.mmorpg.mir.model.country.packet.vo.CountryNpcDamageVO;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.country.resource.CountryFlagResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.collection.ConcurrentHashSet;

/**
 * 国旗
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-15
 * 
 */
public class CountryFlag {
	public static final StatEffectId COUNTRY_FLAG = StatEffectId.valueOf("COUNTRY_FLAG", StatEffectType.COUNTRY);
	public static final String INIT_FLAGID = "1";

	public static final String COUNTRY_FLAG_MODULE_OPENKEY = "opmk25";

	private String flagId;

	@Transient
	private Country country; // 国家
	@Transient
	private CountryNpc countryNpc;// 国家国旗npc
	@Transient
	private Future<?> broadcastFuture;// 广播任务
	@Transient
	private volatile int hiterCountry;
	/** 国旗墓碑 */
	private volatile StatusNpc bombstone;

	/*
	 * @Transient private Map<Integer, Future<?>> attackedStatusFutureMap = new
	 * NonBlockingHashMap<Integer, Future<?>>();
	 */

	private volatile long lastAttactedTime; // 最后被攻击时间
	private volatile int lastAttackCountry; // 最后攻击者的国家

	/** 国旗危险血量通报 */
	private Set<Integer> dangerNoticed = new ConcurrentHashSet<Integer>();

	@Transient
	private Map<Integer, CountryNpcDamageVO> damagedRank = new NonBlockingHashMap<Integer, CountryNpcDamageVO>();

	private Map<Long, Long> pLastAttackTime = new NonBlockingHashMap<Long, Long>();

	@Transient
	private Map<Long, Reward> attendRewardStatus = new NonBlockingHashMap<Long, Reward>();

	private long reliveTime;
	@Transient
	private long deadTime;

	/** 本国被攻击的 国家任务正在进行中 */
	@Transient
	private volatile boolean questing;
	@Transient
	private AtomicBoolean flagQuesting = new AtomicBoolean();
	@Transient
	private long startTime;
	/** 任务类型 @see {@link CountryFlagQuestType} */
	@Transient
	private CountryFlagQuestType type;
	/** 任务 目标国家 */
	@Transient
	private int target;
	/** 任务 的同盟国 如果是2V1的类型的话 */
	@Transient
	private int alliance;
	/** 攻击的国家是2V1的类型的话 */
	@Transient
	private List<Integer> attackCountries = New.arrayList();

	@Transient
	private Future<?> flagQuestTimeOutTask;

	@Transient
	private ArrayList<Creature> allianceSpecialNpcs = New.arrayList();;

	public static CountryFlag valueOf() {
		CountryFlag flag = new CountryFlag();
		flag.flagId = INIT_FLAGID;
		return flag;
	}

	/**
	 * 生成国旗
	 */
	@JsonIgnore
	public void initFlag(Country country) {
		setCountry(country);
	}

	public boolean costExp() {
		if (System.currentTimeMillis() - deadTime <= ConfigValueManager.getInstance().DIED_BUFF_FLAG_EXPTIME.getValue()
				* DateUtils.MILLIS_PER_SECOND) {
			return true;
		}
		return false;
	}

	/**
	 * 生成国旗
	 * 
	 * @return
	 */
	private void spawnCountryFlagNpc(CountryFlagQuestType type) {
		int countryId = country.getId().getValue();
		Map<String, String> cnfMap = ConfigValueManager.getInstance().COUNTRY_DIP_NPC_FLAG.getValue();// getCountryNpcMap_Flag();
		String npcId = cnfMap.get(countryId + "");
		SpawnGroupResource npcSpawn = SpawnManager.getInstance().getSpawn(npcId);
		CountryNpc tempNpc = (CountryNpc) SpawnManager.getInstance().creatObject(npcSpawn.getKey(), 1,
				country.getId().getValue());
		// 替换controller
		CountryNpcFlagAndDachenController controller = new CountryNpcFlagAndDachenController();
		controller.setOwner(tempNpc);
		controller.setCountryFlag();
		tempNpc.setController(controller);
		countryNpc = tempNpc;

		// 添加各种触发器
		countryNpc.getObserveController().addObserver(new ActionObserver(ObserverType.ATTACKED) {
			@Override
			public void attacked(Creature creature) {
				if (creature instanceof Player) {
					npcAttacked((Player) creature);
				} else if (creature instanceof Summon) {
					npcAttacked(((Summon) creature).getMaster());
				}

			}

			@Override
			public void die(Creature creature) {
				if (creature instanceof Summon) {
					endFlagQuesting(((Summon) creature).getMaster());
				} else if (creature instanceof Player) {
					endFlagQuesting(creature);
				}
			}

			@Override
			public void spawn(int mapId, int instanceId) {
				flagSpawnRefresh(); // 通知敌国刷新了
				addAuraSkill(); // 加一个 光环
			}

		});
		SpawnManager.getInstance().bringIntoWorld(tempNpc, 1);

		if (type == CountryFlagQuestType.DEFENCE) {
			for (String spawnKey : ConfigValueManager.getInstance().COUNTRYFLAG_NOT_VISIBLENPC.getValue().get(
					countryId + "")) {
				// CountryNpc allianceNpc = (CountryNpc)
				// SpawnManager.getInstance().creatObject(spawnKey, 1,
				// country.getId().getValue());
				Npc allianceNpc = (Npc) SpawnManager.getInstance().creatObject(spawnKey, 1, country.getId().getValue());
				AllianceSpecialNpcController specialController = new AllianceSpecialNpcController(getCountry().getId());
				specialController.setOwner(allianceNpc);
				allianceNpc.setController(specialController);
				allianceSpecialNpcs.add(allianceNpc);
			}

			for (Creature npc : allianceSpecialNpcs) {
				SpawnManager.getInstance().bringIntoWorld(npc, 1);
			}
		}
	}

	/**
	 * 刷新国旗,发布国家任务
	 */
	synchronized public void spawnFlagAndStartQuest(CountryFlagQuestType type, int target, int alliance,
			List<Integer> attackCountries) {
		if (!flagQuesting.compareAndSet(false, true)) {
			return;
		}
		startTime = System.currentTimeMillis();
		this.type = type;
		this.target = target;
		this.alliance = alliance;
		this.attackCountries = attackCountries;
		attendRewardStatus.clear();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			if (type == CountryFlagQuestType.DEFENCE) {
				country.getCountryFlag().setQuesting(true);
			} else if (attackCountries.contains(country.getId().getValue())) {
				country.getCountryFlag().setQuesting(true);
			}
		}
		clearFlag();

		if (flagQuestTimeOutTask != null)
			flagQuestTimeOutTask.cancel(false);
		// 任务倒计时
		long endTime = ConfigValueManager.getInstance().COUNTRYFLAG_QUEST_DURATION.getValue()
				* DateUtils.MILLIS_PER_MINUTE;
		if (type == CountryFlagQuestType.ATTACK_WITH_ALLIANCE) {
			if (bombstone != null && bombstone.isSpawned()) {
				setReliveTime(ConfigValueManager.getInstance().getNextCountryFlagStartTime());
				bombstone.setStatus((int) (getReliveTime() % 1000000000));
				PacketSendUtility.broadcastPacket(bombstone,
						SM_Country_Flag_Relive_Push.valueOf(hiterCountry, getReliveTime(), country.getId().getValue()));
			}
			flagQuesting.set(false);
		} else {
			flagQuestTimeOutTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					endFlagQuesting(null);
				}
			}, endTime);
			spawnCountryFlagNpc(type);
		}

		SM_CountryFlag_Start sm = SM_CountryFlag_Start.valueOf(getAttackCountries(), target, alliance, startTime);
		getCountry().sendPackAll(sm);
	}

	/**
	 * 关闭国家任务
	 * 
	 * @param creature
	 *            `
	 */
	synchronized public void endFlagQuesting(Creature creature) {
		if (!flagQuesting.compareAndSet(true, false)) {
			return;
		}

		Set<Long> allAttends = new HashSet<Long>();
		allAttends.addAll(attendRewardStatus.keySet());

		boolean attackerWin = (creature != null && countryNpc.getLifeStats().isAlreadyDead());
		boolean hadDefender = type == CountryFlagQuestType.DEFENCE;
		boolean defendWin = (creature == null && hadDefender);
		boolean canCompensate = canCompensate();
		long aheadOfTimes = ConfigValueManager.getInstance().COUNTRYFLAG_QUEST_DURATION.getValue()
				* DateUtils.MILLIS_PER_MINUTE - (System.currentTimeMillis() - startTime);
		
		Set<Long> flagCountryPeople = new HashSet<Long>(getCountry().getCivils().keySet());
		Set<Long> attackers = getAttendAttackers();
		
		if (attackerWin) {	
			reward(CountryFlagQuestType.ATTACK, (byte) 0, allAttends, attackers, canCompensate, aheadOfTimes);
			if (hadDefender) {
				reward(CountryFlagQuestType.DEFENCE, (byte) 1, allAttends, flagCountryPeople,
						canCompensate, aheadOfTimes);
			}
		}
		if (hadDefender && defendWin) {
			reward(CountryFlagQuestType.DEFENCE, (byte) 0, allAttends, flagCountryPeople, false,
					aheadOfTimes);
		}

		if (!attackerWin) {
			reward(CountryFlagQuestType.ATTACK, (byte) 1, allAttends, attackers, false, aheadOfTimes);
		}

		logRank();
		stopBroadcastHelpTask();

		for (Long pid : allAttends) {
			Player p = PlayerManager.getInstance().getPlayer(pid);
			if (!p.getPlayerCountryHistory().isAttendFlag()) {
				PacketSendUtility.sendPacket(p, new SM_AttendFlag_Already());
				p.getPlayerCountryHistory().setAttendFlag(true);
			}
		}

		dangerNoticed.clear();
		pLastAttackTime.clear();

		setReliveTime(ConfigValueManager.getInstance().getNextCountryFlagStartTime());
		spawnBombstone(getReliveTime());

		if (creature != null && creature.getObjectType() == ObjectType.PLAYER) {
			Player lastAttacker = (Player) creature;
			hiterCountry = lastAttacker.getCountryValue();
			PacketSendUtility.broadcastPacket(getCountryNpc(),
					SM_Country_Flag_Relive_Push.valueOf(hiterCountry, getReliveTime(), country.getId().getValue()));
		}

		deadTime = System.currentTimeMillis();
		if (hadDefender) {
			for (Country country : CountryManager.getInstance().getCountries().values()) {
				country.getCountryFlag().setQuesting(false);
			}
			SessionManager.getInstance().sendAllIdentified(SM_CountryFlag_End.valueOf(getReliveTime()));
		} else {
			for (Integer actCountry : attackCountries) {
				Country country = CountryManager.getInstance().getCountryByValue(actCountry);
				country.sendPackAll(SM_CountryFlag_End.valueOf(getReliveTime()));
				country.getCountryFlag().setQuesting(false);
			}
		}

		// 国旗消失
		if (countryNpc != null) {
			countryNpc.getController().onFightOff();
			countryNpc.getEffectController().dieRemoveAllEffects();
			countryNpc.getController().delete();
		}

		for (Creature allianceNpc : allianceSpecialNpcs) {
			allianceNpc.getController().delete();
		}

		allianceSpecialNpcs.clear();
	}

	private void reward(CountryFlagQuestType type, byte lost, Set<Long> buffAttenders, Collection<Long> attendResultSet,
			boolean canCompensate, long aheadOfTimes) {
 		String rewardChoserIds = ConfigValueManager.getInstance().getCountryFlagRewardId(type, lost);
		String fullMailRewardIds = ConfigValueManager.getInstance().getFlagRewardFullRewardId(type, lost);
		I18nUtils mailTitle = I18nUtils
				.valueOf(ConfigValueManager.getInstance().getFlagFullRewardMailTitle(type, lost));
		I18nUtils mailContent = I18nUtils.valueOf(ConfigValueManager.getInstance().getFlagFullRewardMailContent(type,
				lost));
		for (Long pid: attendResultSet) {
			Player player = PlayerManager.getInstance().getPlayer(pid);
			if (player == null) {
				continue;
			}
			if (!ConfigValueManager.getInstance().filterFlagAttend(player)) {
				continue;
			}
			if (!buffAttenders.contains(player.getObjectId())) {
				continue;
			}
			boolean isProtectGuy = type == CountryFlagQuestType.DEFENCE
					&& player.getCountryId() == getCountry().getId();
			boolean isAttackerGuy = type != CountryFlagQuestType.DEFENCE
					&& attackCountries.contains(player.getCountryValue());

			if (!isAttackerGuy && !isProtectGuy) {
				continue;
			}

			Map<String, Object> params = New.hashMap();
			params.put("LEVEL", player.getLevel());
			params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
			params.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
			if (canCompensate && !player.getPlayerCountryHistory().hiddenMissionFinished(HiddenMissionType.DEFEND_FLAG)) {
				attendCompensateMail(player, isAttackerGuy, aheadOfTimes);
			}
			if (player.getPlayerCountryHistory().takeMission(HiddenMissionType.DEFEND_FLAG)) {
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, rewardChoserIds);
				Reward reward = RewardManager.getInstance().grantReward(
						player,
						rewardIds,
						ModuleInfo.valueOf(ModuleType.FLAG_QUEST, SubModuleType.COUNTRY_WAR_REWARD,
								HiddenMissionType.DEFEND_FLAG.name()), params);
				logAttendReward(player, reward);
				int leftCount = CountryManager.getInstance().getHiddenMissionLeftCount(player,
						HiddenMissionType.DEFEND_FLAG.getValue());
				SM_Country_War_Reward sm = SM_Country_War_Reward.valueOf(country.getId().getValue(),
						HiddenMissionType.DEFEND_FLAG.getValue(), leftCount, lost,
						attendRewardStatus.get(player.getObjectId()));
				PacketSendUtility.sendPacket(player, sm);
			} else {
				Reward r = RewardManager.getInstance().creatReward(player, fullMailRewardIds, params);
				Mail mail = Mail.valueOf(mailTitle, mailContent, null, r);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				logAttendReward(player, r);
			}
			// dispacther event
			EventBusManager.getInstance().submit(CountryFlagQuestFinishEvent.valueOf(player.getObjectId(), 1));
			if (lost == 0 && player.getCountry().getReserveKing().isReserveKing(player.getObjectId())) {
				EventBusManager.getInstance().submit(ReserveKingFinishFlagEvent.valueOf(player.getObjectId()));
			}
		}
	}

	/**
	 * 在国家NPC上添加一个释放属性buff的光环技能
	 */
	private void addAuraSkill() {
		// TODO 添加一个新的加国旗的光环效果
		Skill skill = SkillEngine.getInstance().getSkill(null,
				ConfigValueManager.getInstance().COUNTRY_FLAG_ATTEND_SKILL.getValue(), countryNpc.getObjectId(), 0, 0,
				countryNpc, null);
		skill.noEffectorUseSkill();
	}

	/**
	 * 脱战
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isNotAttacked() {
		if (lastAttactedTime + ConfigValueManager.getInstance().COUNTRY_FLAG_LEAVE_ATTACKED_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * 国旗被砍
	 */
	private void npcAttacked(final Player hiter) {
		if (!attackCountries.contains(hiter.getCountryValue())) { // 过滤非目标国
			return;
		}
		lastAttactedTime = System.currentTimeMillis();
		lastAttackCountry = hiter.getCountryValue();
		pLastAttackTime.put(hiter.getObjectId(), System.currentTimeMillis());
		if (getNpcHpPercentage() <= 50) { // 低于血量50% 才开始HELP
			startBroadcastHelpTask(hiter);
		}
		startBroadcastDanger(hiter);
	}

	private void logRank() {
		Map<Player, Long> playerDamages = countryNpc.getAggroList().getPlayerDamage();
		Map<Integer, Player> rankDamages = countryNpc.getAggroList().getPlayerDamageRank(playerDamages);
		for (int i = 1; i <= ConfigValueManager.getInstance().RANK_SIZE.getValue(); i++) {
			if (!rankDamages.containsKey(i)) {
				break;
			}
			Player player = rankDamages.get(i);
			CountryNpcDamageVO vo = CountryNpcDamageVO.valueOf(i, player, attendRewardStatus.get(player.getObjectId()),
					playerDamages.get(player));
			damagedRank.put(i, vo);
		}
	}

	/** 通报危险血量 */
	synchronized private void startBroadcastDanger(Player hiter) {
		for (Entry<String, String> entry : ConfigValueManager.getInstance().getCountryNpcHpTipFlag().entrySet()) {// getHpTipsFlag().entrySet())
																													// {
			Integer hp = Integer.valueOf(entry.getKey());
			if (!dangerNoticed.contains(hp)) {
				if (Integer.valueOf(entry.getKey()) >= getNpcHpPercentage()) {
					// Broadcast.getInstance().broadcastCallCountryFlagDanger(hiter,
					// country, hp);
					Broadcast.getInstance().broadcastCallByHiterFlag(hiter, country, hp);
					dangerNoticed.add(Integer.valueOf(hp));
				}
			}
		}
	}

	private void flagSpawnRefresh() {
		setReliveTime(0);
		dangerNoticed.clear();
		// 让墓碑消失
		if (bombstone != null) {
			bombstone.getController().delete();
		}

		damagedRank.clear();
	}

	synchronized private void stopBroadcastHelpTask() {
		if (broadcastFuture != null && !broadcastFuture.isCancelled()) {
			broadcastFuture.cancel(true);
		}
	}

	private Set<Long> getAttendAttackers() {
		Map<Player, Long> players = getCountryNpc().getAggroList().getSpecifiedCountryDamagePlayers(attackCountries);
		for (Entry<Long, Long> entry : pLastAttackTime.entrySet()) { // 一分钟内有伤害的人
			if (System.currentTimeMillis() - entry.getValue() <= DateUtils.MILLIS_PER_MINUTE) {
				Player bPlayer = PlayerManager.getInstance().getPlayer(entry.getKey());
				if (attackCountries.contains(bPlayer.getCountryValue()) && (!players.containsKey(bPlayer))) {
					players.put(bPlayer, 0L);
				}
			}
		}

		for (VisibleObject visObj : getCountryNpc().getKnownList()) { // 没有伤害的
																		// 围观者
			if (visObj instanceof Player) {
				Player knowPlayer = (Player) visObj;
				if (attackCountries.contains(knowPlayer.getCountryValue())) {
					players.put(knowPlayer, 0L);
				}
			}
		}

		for (Long attendIds : attendRewardStatus.keySet()) {
			Player player = PlayerManager.getInstance().getPlayer(attendIds);
			if (!players.containsKey(player)) {
				players.put(player, 0L);
			}
		}
		
		Set<Long> pids = new HashSet<Long>(players.size());
		for (Player player : players.keySet()) {
			pids.add(player.getObjectId());
		}
		return pids;
	}

	public void doBuffFloor() {
		for (Integer countryValue : attackCountries) {
			Country attackerCountry = CountryManager.getInstance().getCountries().get(CountryId.valueOf(countryValue));
			attackerCountry.clearBuffFloor();
		}
		country.addBuffFloor(1);
	}

	@JsonIgnore
	public int getNpcHpPercentage() {
		return (int) countryNpc.getLifeStats().getHpPercentage();
	}

	synchronized private void startBroadcastHelpTask(final Player hiter) {
		if (broadcastFuture == null || broadcastFuture.isCancelled()) {
			final int seconds = ConfigValueManager.getInstance().COUNTRY_NPC_BROADCAST_SECOND.getValue();// getBroadcastSeconds().getValue();
			broadcastFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (countryNpc.getLifeStats().isAlreadyDead() || isNotAttacked() || getNpcHpPercentage() > 50) {
						stopBroadcastHelpTask();
					}
					Broadcast.getInstance().broadcastCallCountryFlagHelp(hiter, country);
					Broadcast.getInstance().broadcastCallByFightingFlag(hiter, country);
				}
			}, DateUtils.MILLIS_PER_SECOND * seconds, DateUtils.MILLIS_PER_SECOND * seconds);
		}
	}

	@JsonIgnore
	public CountryFlagResource getResource() {
		return CountryManager.getInstance().getCountryFlagResources().get(flagId, true);
	}

	@JsonIgnore
	public void upgradeFlag(String nextFlagId) {
		this.flagId = nextFlagId;
		getCountry().sendPacket(SM_Country_UpgradeFlag.valueOf(nextFlagId));
		for (Player player : country.getCivils().values()) {
			if (ModuleOpenManager.getInstance().isOpenByKey(player, CountryFlag.COUNTRY_FLAG_MODULE_OPENKEY)) {
				player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
						player.getCountry().getCountryFlag().getResource().getPlayerStats());
			}
		}
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	@JsonIgnore
	public void setCountry(Country country) {
		this.country = country;
	}

	@JsonIgnore
	public CountryNpc getCountryNpc() {
		return countryNpc;
	}

	@JsonIgnore
	public void setCountryNpc(CountryNpc countryNpc) {
		this.countryNpc = countryNpc;
	}

	public long getReliveTime() {
		return reliveTime;
	}

	public void setReliveTime(long reliveTime) {
		this.reliveTime = reliveTime;
	}

	@JsonIgnore
	public int getHiterCountry() {
		if (countryNpc == null || !countryNpc.getLifeStats().isAlreadyDead()) {
			return 0;
		}
		return hiterCountry;
	}

	@JsonIgnore
	public long getDeadTime() {
		if (countryNpc == null || !countryNpc.getLifeStats().isAlreadyDead()) {
			return 0L;
		}
		return deadTime;
	}

	@JsonIgnore
	public int getLastAttackCountry() {
		return lastAttackCountry;
	}

	@JsonIgnore
	public Map<Integer, CountryNpcDamageVO> getDamagedRank() {
		return damagedRank;
	}

	@JsonIgnore
	public CountryFlagQuestType getFlagQuestType() {
		return type;
	}

	@JsonIgnore
	public int getAlliance() {
		if (type == CountryFlagQuestType.ATTACK_WITH_ALLIANCE) {
			return alliance;
		}
		return 0;
	}

	@JsonIgnore
	public boolean isFlagQuesting() {
		return flagQuesting.get();
	}

	@JsonIgnore
	public long questStartTime() {
		return startTime;
	}

	@JsonIgnore
	public int getTarget() {
		return target;
	}

	@JsonIgnore
	public Reward logAttendReward(Player player, Reward reward) {
		Reward original = attendRewardStatus.get(player.getObjectId());
		if (original == null) {
			original = reward;
		} else {
			original.addReward(reward);
		}
		attendRewardStatus.put(player.getObjectId(), original);
		return original;
	}

	public void spawnBombstone(long nextReliveTime) {
		if (bombstone == null || !bombstone.isSpawned()) {
			// 生成墓碑
			String bombstoneId = ConfigValueManager.getInstance().getCountryFlagBombStoneId(this.country.getId());
			bombstone = (StatusNpc) SpawnManager.getInstance().spawnObject(bombstoneId, 1); // 这个id要读配置表
			bombstone.setStatus((int) (nextReliveTime % 1000000000));
		}
	}

	@JsonIgnore
	public long getNextReliveTime() {
		boolean isAlive = (countryNpc != null && !countryNpc.getLifeStats().isAlreadyDead());
		if (!ServerState.getInstance().isOpenServer() || (isFlagQuesting() && isAlive)) {
			return 0L;
		} else if (reliveTime != 0 && reliveTime > System.currentTimeMillis()) {
			return reliveTime;
		} else {
			return ConfigValueManager.getInstance().getNextCountryFlagStartTime();
		}
	}

	@JsonIgnore
	public ArrayList<Integer> getAttackCountries() {
		return new ArrayList<Integer>(attackCountries);
	}

	@JsonIgnore
	public boolean isQuesting() {
		return questing;
	}

	@JsonIgnore
	public void setQuesting(boolean questing) {
		this.questing = questing;
	}

	private void attendCompensateMail(Player player, boolean attacker, long interval) {
		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		params.put("LEFT", interval / 1000);
		if (attacker) {
			Reward reward = RewardManager.getInstance().creatReward(player,
					ConfigValueManager.getInstance().CUT_ATTEND_REWARD.getValue(), params);
			I18nUtils title = I18nUtils.valueOf(ConfigValueManager.getInstance().CUT_ATTEND_MAIL_TITLE.getValue());
			I18nUtils context = I18nUtils.valueOf(ConfigValueManager.getInstance().CUT_ATTEND_MAIL_CONTENT.getValue());
			context.addParm("country", I18nPack.valueOf(getCountry().getName()));
			Mail mail = Mail.valueOf(title, context, null, reward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		} else {
			Reward reward = RewardManager.getInstance().creatReward(player,
					ConfigValueManager.getInstance().CUT_ATTEND_REWARD.getValue(), params);
			I18nUtils title = I18nUtils.valueOf(ConfigValueManager.getInstance().DEFEND_ATTEND_MAIL_TITLE.getValue());
			I18nUtils context = I18nUtils.valueOf(ConfigValueManager.getInstance().DEFEND_ATTEND_MAIL_CONTENT
					.getValue());
			Mail mail = Mail.valueOf(title, context, null, reward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
	}

	private boolean canCompensate() {
		long interval = ConfigValueManager.getInstance().COUNTRYFLAG_QUEST_DURATION.getValue()
				* DateUtils.MILLIS_PER_MINUTE - (System.currentTimeMillis() - startTime);
		return interval >= ConfigValueManager.getInstance().COMPENSATE_LEFT_SECOND_COND.getValue() * 1000;
	}

	private void clearFlag() {
		int countryId = country.getId().getValue();
		Map<String, String> cnfMap = ConfigValueManager.getInstance().COUNTRY_DIP_NPC_FLAG.getValue();// getCountryNpcMap_Flag();
		String npcId = cnfMap.get(countryId + "");
		SpawnGroupResource npcSpawn = SpawnManager.getInstance().getSpawn(npcId);
		List<VisibleObject> npcs = World.getInstance().getWorldMap(npcSpawn.getMapId()).getInstances().get(1)
				.findObjectBySpawnId(npcSpawn.getKey());
		for (VisibleObject flag : npcs) {
			flag.getController().delete();
		}
		if (countryNpc != null) {
			countryNpc.getController().delete();
		}

		for (Creature creature : allianceSpecialNpcs) {
			creature.getController().delete();
		}

		allianceSpecialNpcs.clear();
	}

	@JsonIgnore
	public Reward getAttendRewardStatus(Player player) {
		Reward reward = attendRewardStatus.get(player.getObjectId());
		return reward == null ? Reward.valueOf() : reward;
	}

	@JsonIgnore
	public ArrayList<Creature> getSpecifiedNpcs() {
		return allianceSpecialNpcs;
	}

}
