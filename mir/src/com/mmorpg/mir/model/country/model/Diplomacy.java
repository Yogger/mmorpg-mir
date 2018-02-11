package com.mmorpg.mir.model.country.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.country.broadcast.Broadcast;
import com.mmorpg.mir.model.country.controllers.CountryNpcFlagAndDachenController;
import com.mmorpg.mir.model.country.event.PlayerKillDiplomacyEvent;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.countryact.CountryWarEvent;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.packet.SM_CountryNpc_Rank_Sign;
import com.mmorpg.mir.model.country.packet.SM_Country_Close_Event;
import com.mmorpg.mir.model.country.packet.SM_Country_Diplomacy_Reilve_Push;
import com.mmorpg.mir.model.country.packet.SM_Country_War_Event;
import com.mmorpg.mir.model.country.packet.SM_Country_War_Reward;
import com.mmorpg.mir.model.country.packet.vo.CountryNpcDamageVO;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effecttemplate.CountryAuraEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;
import com.windforce.common.utility.collection.ConcurrentHashSet;

/**
 * 
 * 外交
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-15
 * 
 */
public class Diplomacy {

	private Map<Byte, Long> callTogetherTokens = new HashMap<Byte, Long>();
	@Transient
	private Country country; // 国家
	@Transient
	private CountryNpc countryNpc;// 国家大臣npc
	@Transient
	private Future<?> broadcastFuture;// 伤害广播任务
	@Transient
	private Future<?> storeHpFuture;// 回血任务
	private volatile long lastAttactedTime; // 最后被攻击时间
	private volatile int lastAttackCountry; // 最后攻击者的国家

	// 大臣墓碑
	private volatile StatusNpc bombStone;

	/** 大臣危险血量已经通报 */
	private Set<Integer> dangerNoticed = new ConcurrentHashSet<Integer>();
	private Set<Integer> attackerCountryNoticed = new ConcurrentHashSet<Integer>();

	@Transient
	private Map<Integer, Future<?>> attackedStatusFutureMap = new NonBlockingHashMap<Integer, Future<?>>();

	@Transient
	private Map<Integer, CountryNpcDamageVO> damagedRank = new NonBlockingHashMap<Integer, CountryNpcDamageVO>();
	@Transient
	private Map<Long, Reward> logPlayerReward = new NonBlockingHashMap<Long, Reward>();

	private Map<Long, Long> pLastAttackTime = new NonBlockingHashMap<Long, Long>();

	private long reliveTime;
	@Transient
	private int hiterCountry;
	@Transient
	private long deadTime;

	/**
	 * 初始化国家战斗NPC
	 */
	public void initCountryBattleNpc(Country country) {
		setCountry(country);
	}

	public void spawnDiplomacyNpc() {
		if (countryNpc != null) {
			countryNpc.getController().delete();
		}
		int countryId = country.getId().getValue();
		Map<String, String> cnfMap = ConfigValueManager.getInstance().COUNTRY_DIP_NPC.getValue();
		String npcSpawnkey = cnfMap.get(countryId + "");
		countryNpc = (CountryNpc) SpawnManager.getInstance().creatObject(npcSpawnkey, 1);
		CountryNpcFlagAndDachenController controller = new CountryNpcFlagAndDachenController();
		controller.setOwner(countryNpc);
		countryNpc.setController(controller);
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
				if (creature instanceof Player) {
					npcDie((Player) creature);
				} else if (creature instanceof Summon) {
					npcDie(((Summon) creature).getMaster());
				}
			}

			@Override
			public void spawn(int mapId, int instanceId) {
				relive();
			}

			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject instanceof Player) {
					if (countryNpc.isEnemy((Player) visibleObject)) {
						CountryAuraEffect.doApplyAuraTo(countryNpc, (Player) visibleObject,
								ConfigValueManager.getInstance().AURA_APPLYTO_STATSKILLID.getValue());
					}
				}
			}

		});
		SpawnManager.getInstance().bringIntoWorld(countryNpc, 1);
		addAuraSkill();
	}

	/**
	 * 大臣被砍
	 */
	private void npcAttacked(final Player hiter) {
		lastAttactedTime = System.currentTimeMillis();
		lastAttackCountry = hiter.getCountryValue();
		pLastAttackTime.put(hiter.getObjectId(), System.currentTimeMillis());

		Future<?> attackedStatusFuture = attackedStatusFutureMap.get(country.getId().getValue());
		Future<?> attackCountryFuture = attackedStatusFutureMap.get(hiter.getCountryValue());

		if (attackedStatusFuture == null || attackedStatusFuture.isCancelled() || attackedStatusFuture.isDone()) {
			if (!countryNpc.getLifeStats().isAlreadyDead()) {
				country.sendPackAll(SM_Country_War_Event.valueOf(country.getId().getValue(),
						CountryWarEvent.DIPLOMACY_UNDER_ATTACK.getValue()), ConfigValueManager.getInstance()
						.getCountryWarPushCond(CountryWarEvent.DIPLOMACY_UNDER_ATTACK.getValue()));
			}
			attackedStatusFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(
					new Runnable() {
						@Override
						public void run() {
							if (isNotAttacked() && (!countryNpc.getLifeStats().isAlreadyDead())) {
								country.sendPackAll(
										SM_Country_Close_Event.valueOf(CountryWarEvent.DIPLOMACY_UNDER_ATTACK
												.getValue(), country.getId().getValue()),
										ConfigValueManager.getInstance().getCountryWarPushCond(
												CountryWarEvent.DIPLOMACY_UNDER_ATTACK.getValue()));
								cancalSpecificAttack(country.getId().getValue());
								countryNpc.getController().onFightOff();
							}
						}
					},
					ConfigValueManager.getInstance().COUNTRY_FLAG_LEAVE_ATTACKED_TIME.getValue()
							* DateUtils.MILLIS_PER_SECOND,
					ConfigValueManager.getInstance().COUNTRY_FLAG_LEAVE_ATTACKED_TIME.getValue()
							* DateUtils.MILLIS_PER_SECOND);
			Future<?> before = attackedStatusFutureMap.put(country.getId().getValue(), attackedStatusFuture);
			if (before != null) {
				before.cancel(false);
			}
		}

		if (attackCountryFuture == null || attackCountryFuture.isCancelled()) {
			if (!countryNpc.getLifeStats().isAlreadyDead()) {
				hiter.getCountry().sendPackAll(
						SM_Country_War_Event.valueOf(country.getId().getValue(),
								CountryWarEvent.ATTACK_DIPLOMACY.getValue()),
						ConfigValueManager.getInstance().getCountryWarPushCond(
								CountryWarEvent.ATTACK_DIPLOMACY.getValue()));
			}

			attackCountryFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(
					new Runnable() {
						@Override
						public void run() {
							if (isNotAttacked() && (!countryNpc.getLifeStats().isAlreadyDead())) {
								hiter.getCountry().sendPackAll(
										SM_Country_Close_Event.valueOf(CountryWarEvent.ATTACK_DIPLOMACY.getValue(),
												country.getId().getValue()),
										ConfigValueManager.getInstance().getCountryWarPushCond(
												CountryWarEvent.ATTACK_DIPLOMACY.getValue()));
								cancalSpecificAttack(hiter.getCountryValue());
							}
						}
					},
					ConfigValueManager.getInstance().COUNTRY_FLAG_LEAVE_ATTACKED_TIME.getValue()
							* DateUtils.MILLIS_PER_SECOND,
					ConfigValueManager.getInstance().COUNTRY_FLAG_LEAVE_ATTACKED_TIME.getValue()
							* DateUtils.MILLIS_PER_SECOND);
			Future<?> before = attackedStatusFutureMap.put(hiter.getCountryValue(), attackCountryFuture);
			if (before != null) {
				before.cancel(false);
			}
		}

		if (getNpcHpPercentage() <= 50) { // 低于血量的50% 才开始help 公告
			startBroadcastHelpTask(hiter);
		}
		restoreHp();
		startBroadcastDanger(hiter);
	}

	/**
	 * 大臣死亡
	 */
	private void npcDie(Player hiter) {
		stopBroadcastTask();
		// 扣除统治力
		Integer value = ConfigValueManager.getInstance().COUNTRY_DIP_NPC_DIE.getValue();
		getCountry().getCourt().reduceControl(value);
		int mostDamageCountry = rewardForKnownList();
		Broadcast.getInstance().broadcastDipDie(mostDamageCountry, country);
		Country attackerCountry = CountryManager.getInstance().getCountries().get(CountryId.valueOf(mostDamageCountry));
		attackerCountry.sendPackAllExceptCollections(SM_CountryNpc_Rank_Sign.valueOf((byte) 1, country.getId()
				.getValue()), ConfigValueManager.getInstance().getAttendDiplomacyCond(), logPlayerReward.keySet());

		logRank(mostDamageCountry);
		setReliveTime(ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime());

		spawnBombstone(getReliveTime());

		hiterCountry = mostDamageCountry;
		deadTime = System.currentTimeMillis();
		PacketSendUtility.broadcastPacket(countryNpc, SM_Country_Diplomacy_Reilve_Push.valueOf(hiterCountry, this));
		for (CountryId id : CountryId.values()) {
			if (cancalSpecificAttack(id.getValue())) {
				Country c = CountryManager.getInstance().getCountries().get(id);
				if (id == country.getId()) {
					c.sendPackAll(
							SM_Country_Close_Event.valueOf(CountryWarEvent.DIPLOMACY_UNDER_ATTACK.getValue(), country
									.getId().getValue()),
							ConfigValueManager.getInstance().getCountryWarPushCond(
									CountryWarEvent.DIPLOMACY_UNDER_ATTACK.getValue()));
				} else {
					c.sendPackAll(
							SM_Country_Close_Event.valueOf(CountryWarEvent.ATTACK_DIPLOMACY.getValue(), country.getId()
									.getValue()),
							ConfigValueManager.getInstance().getCountryWarPushCond(
									CountryWarEvent.ATTACK_DIPLOMACY.getValue()));
				}
			}
		}
		countryNpc.getController().onFightOff();
		pLastAttackTime.clear();
	}

	private void logRank(int mostdamageCountry) {
		Map<Player, Long> playerDamages = countryNpc.getAggroList().getMostDamageCountryPlayers();
		Map<Integer, Player> rankDamages = countryNpc.getAggroList().getPlayerDamageRank(playerDamages);
		for (int i = 1; i <= ConfigValueManager.getInstance().RANK_SIZE.getValue(); i++) {
			if (!rankDamages.containsKey(i)) {
				break;
			}
			Player player = rankDamages.get(i);
			if (mostdamageCountry != player.getCountryValue()) {
				continue;
			}
			CountryNpcDamageVO vo = CountryNpcDamageVO.valueOf(i, player, logPlayerReward.get(player.getObjectId()),
					playerDamages.get(player));
			damagedRank.put(i, vo);
		}
		logPlayerReward.clear();
	}

	/** 停止广播求救任务 */
	private void stopBroadcastTask() {
		if (broadcastFuture != null && !broadcastFuture.isCancelled()) {
			broadcastFuture.cancel(true);
		}
	}

	private boolean cancalSpecificAttack(int countryId) {
		Future<?> future = attackedStatusFutureMap.get(countryId);
		if (future != null) {
			return future.cancel(false);
		}
		return false;
	}

	/**
	 * 在国家NPC上添加一个释放属性buff的光环技能
	 */
	private void addAuraSkill() {
		Skill skill = SkillEngine.getInstance().getSkill(null,
				ConfigValueManager.getInstance().AURA_STATSKILLID.getValue(), countryNpc.getObjectId(), 0, 0,
				countryNpc, null);
		skill.noEffectorUseSkill();
	}

	private void relive() {
		setReliveTime(ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime());
		dangerNoticed.clear();
		attackerCountryNoticed.clear();

		// 让墓碑消失
		if (bombStone != null) {
			bombStone.getController().delete();
		}
		// I18nUtils utils = I18nUtils.valueOf("10312");
		// utils.addParm("country", I18nPack.valueOf(country.getName()));
		// utils.addParm("npc", I18nPack.valueOf(countryNpc.getName()));
		// ChatManager.getInstance().sendSystem(11001, utils, null);
		// I18nUtils chatUtils = I18nUtils.valueOf("301029", utils);
		// ChatManager.getInstance().sendSystem(0, chatUtils, null);
		for (Country c : CountryManager.getInstance().getCountries().values()) {
			if (!c.getId().equals(country.getId())) {
				c.sendPackAll(SM_Country_War_Event.valueOf(country.getId().getValue(),
						CountryWarEvent.ENEMY_DIPLOMACY_RELIVE.getValue()), ConfigValueManager.getInstance()
						.getCountryWarPushCond(CountryWarEvent.ENEMY_DIPLOMACY_RELIVE.getValue()));
			}
		}

		damagedRank.clear();
		addAuraSkill();
	}

	/**
	 * 脱战
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isNotAttacked() {
		if (lastAttactedTime + ConfigValueManager.getInstance().COUNTRY_DIP_LEAVE_ATTACKED_TIME.getValue()
				* DateUtils.MILLIS_PER_SECOND < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/** 大臣被攻击后每30秒发送一次求救广播 */
	synchronized private void startBroadcastHelpTask(final Player hiter) {
		if (broadcastFuture == null || broadcastFuture.isCancelled()) {
			final int seconds = ConfigValueManager.getInstance().COUNTRY_NPC_BROADCAST_SECOND.getValue();
			broadcastFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (countryNpc.getLifeStats().isAlreadyDead() || isNotAttacked() || getNpcHpPercentage() > 50) {
						stopBroadcastTask();
					}
					Broadcast.getInstance().broadcastCallDiplomacyHelp(hiter, country);
					Broadcast.getInstance().broadcastCallByFightingDip(hiter, country);
				}
			}, DateUtils.MILLIS_PER_SECOND * seconds, DateUtils.MILLIS_PER_SECOND * seconds);
		}
	}

	/** 通报危险血量 */
	synchronized private void startBroadcastDanger(Player hiter) {
		for (Entry<String, String> entry : ConfigValueManager.getInstance().getCountryNpcHpTipDiplomacy().entrySet()) {
			Integer hp = Integer.valueOf(entry.getKey());
			if (!dangerNoticed.contains(hp)) {
				if (Integer.valueOf(entry.getKey()) >= getNpcHpPercentage()) {
					Broadcast.getInstance().broadcastCallDiplomacyDanger(hiter, country, hp);
					Broadcast.getInstance().broadcastCallByHiterDip(hiter, country, hp);
					dangerNoticed.add(hp);
				}
			}
		}
		for (Integer hp : ConfigValueManager.getInstance().HITER_DIPLOMACY_HP_TIPS.getValue()) {
			if (!attackerCountryNoticed.contains(hp)) {
				if (hp >= getNpcHpPercentage()) {
					Broadcast.getInstance().broadcastCallByHiterDipChat(hiter, country, hp);
					attackerCountryNoticed.add(hp);
				}
			}
		}
	}

	/**
	 * 回血任务
	 */
	synchronized private void restoreHp() {
		final long millis = ConfigValueManager.getInstance().COUNTRY_NPC_BACK_HP_TIME.getValue() * 1000;
		if (storeHpFuture == null || storeHpFuture.isCancelled()) {
			storeHpFuture = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (isNotAttacked() && !countryNpc.getLifeStats().isFullyRestoredHp()) {
						int restoreHpValue = (int) Math.ceil((countryNpc.getObjectResource().getRestoreHp() * 1.0 / 10000)
								* countryNpc.getLifeStats().getMaxHp());
						countryNpc.getLifeStats().increaseHp(restoreHpValue);
					}

					if (countryNpc.getLifeStats().isFullyRestoredHp()) {
						dangerNoticed.clear();
						attackerCountryNoticed.clear();
						// 满血关闭
						storeHpFuture.cancel(false);
					}
				}
			}, millis, millis);
		}
	}

	/** 给攻击方发奖励,附近对大臣造成伤害的玩家 */
	private int rewardForKnownList() {
		Map<Player, Long> players = countryNpc.getAggroList().getMostDamageCountryPlayers();
		int mostDamageCountryValue = players.keySet().iterator().next().getCountryValue();

		for (Entry<Long, Long> entry : pLastAttackTime.entrySet()) {
			if (System.currentTimeMillis() - entry.getValue() <= DateUtils.MILLIS_PER_MINUTE) {
				Player bPlayer = PlayerManager.getInstance().getPlayer(entry.getKey());
				if (bPlayer.getCountryValue() == mostDamageCountryValue && (!players.containsKey(bPlayer))) {
					players.put(bPlayer, 0L);
				}
			}
		}

		for (VisibleObject visObj : getCountryNpc().getKnownList()) { // 没有伤害的
																		// 围观者
			if (visObj instanceof Player) {
				Player knowPlayer = (Player) visObj;
				if (knowPlayer.getCountryValue() == mostDamageCountryValue) {
					players.put(knowPlayer, 0L);
				}
			}
		}
		Country mostDamageCountry = CountryManager.getInstance().getCountries()
				.get(CountryId.valueOf(mostDamageCountryValue));
		mostDamageCountry.clearBuffFloor();

		for (Player player : players.keySet()) {
			boolean reward = false;
			if (getCountryNpc().getKnownList().knowns(player)) {
				reward = true;
			} else {
				Long lastDamageTime = pLastAttackTime.get(player.getObjectId());
				if (lastDamageTime != null
						&& (System.currentTimeMillis() - lastDamageTime <= DateUtils.MILLIS_PER_MINUTE)) {
					reward = true;
				}
			}
			if (reward) {
				if (player.getPlayerCountryHistory().takeMission(HiddenMissionType.CUT_DIPOMACY_DOWN)) {
					String rewardChoserId = ConfigValueManager.getInstance().HIDDEN_MISSION_REWARDID.getValue().get(
							HiddenMissionType.CUT_DIPOMACY_DOWN.name());
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, rewardChoserId);
					Map<String, Object> map = New.hashMap();
					map.put("LEVEL", player.getLevel());
					map.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
					map.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
					Reward r = RewardManager.getInstance().creatReward(player, rewardIds, map);
					RewardManager.getInstance().grantReward(player, r,
							ModuleInfo.valueOf(ModuleType.HIDDEN_MISSION, SubModuleType.CUT_DIPLOMACY));
					logPlayerReward.put(player.getObjectId(), r);
					int leftCount = CountryManager.getInstance().getHiddenMissionLeftCount(player,
							HiddenMissionType.CUT_DIPOMACY_DOWN.getValue());
					PacketSendUtility.sendPacket(player, SM_Country_War_Reward.valueOf(country.getId().getValue(),
							HiddenMissionType.CUT_DIPOMACY_DOWN.getValue(), leftCount, r));
				} else {
					Map<String, Object> params = New.hashMap();
					params.put("LEVEL", player.getLevel());
					params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
					params.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
					Reward r = RewardManager.getInstance().creatReward(player,
							ConfigValueManager.getInstance().CUT_DIPLOMACY_ATTEND_REWARD.getValue(), params);
					Mail mail = Mail.valueOf(
							I18nUtils.valueOf(ConfigValueManager.getInstance().CUT_DIPLOMACY_TITLE.getValue()),
							I18nUtils.valueOf(ConfigValueManager.getInstance().CUT_DIPLOMACY_CONTENT.getValue()), null,
							r);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					logPlayerReward.put(player.getObjectId(), r);
				}
				EventBusManager.getInstance().submit(PlayerKillDiplomacyEvent.valueOf(player.getObjectId()));
			}
		}
		country.addBuffFloor(1);
		return mostDamageCountryValue;
	}

	@JsonIgnore
	public int getNpcHpPercentage() {
		return (int) countryNpc.getLifeStats().getHpPercentage();
	}

	@JsonIgnore
	public byte hasTogetherToken(long playerId) {
		for (Entry<Byte, Long> entry : callTogetherTokens.entrySet()) {
			if (entry.getValue().longValue() == playerId) {
				return entry.getKey();
			}
		}
		return 0;
	}

	@JsonIgnore
	public void useTogetherToken(long playerId) {
		for (Entry<Byte, Long> entry : callTogetherTokens.entrySet()) {
			if (entry.getValue().longValue() == playerId) {
				callTogetherTokens.remove(entry.getKey());
				return;
			}
		}
	}

	@JsonIgnore
	public byte addCallTogetherToken(long playerId) {
		if (callTogetherTokens.containsKey((byte) 1)) {
			callTogetherTokens.put((byte) 2, playerId);
			return (byte) 2;
		} else {
			callTogetherTokens.put((byte) 1, playerId);
			return (byte) 1;
		}
	}

	@JsonIgnore
	public void clearTToken() {
		getCallTogetherTokens().clear();
	}

	public Map<Byte, Long> getCallTogetherTokens() {
		return callTogetherTokens;
	}

	public void setCallTogetherTokens(Map<Byte, Long> callTogetherTokens) {
		this.callTogetherTokens = callTogetherTokens;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	@JsonIgnore
	public CountryNpc getCountryNpc() {
		return countryNpc;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public long getReliveTime() {
		return reliveTime;
	}

	public void setReliveTime(long reliveTime) {
		this.reliveTime = reliveTime;
	}

	@JsonIgnore
	public int getHiterCountry() {
		return hiterCountry;
	}

	@JsonIgnore
	public long getDeadTime() {
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
	public void spawnBombstone(long nextReliveTime) {
		if (bombStone == null || !bombStone.isSpawned()) {
			// 生成墓碑
			String bombstoneId = ConfigValueManager.getInstance().getCountryDispomacyBombStoneId(this.country.getId());
			bombStone = (StatusNpc) SpawnManager.getInstance().spawnObject(bombstoneId, 1); // 这个id要读配置表
			bombStone.setStatus((int) (nextReliveTime % 1000000000));
		}
	}

	@JsonIgnore
	public long getNextReliveTime() {
		/*
		 * if (!ServerState.getInstance().getNeverStartDiplomacy() ||
		 * (countryNpc != null && !countryNpc.getLifeStats().isAlreadyDead())) {
		 * return 0L; } else if (ServerState.getInstance().isTodayOpenServer())
		 * { Date openDate = ServerState.getInstance().getOpenServerDate(); long
		 * startTime = openDate.getTime() +
		 * ConfigValueManager.getInstance().OPENSERVER_DIPLOMACY_SPAWN
		 * .getValue() * DateUtils.MILLIS_PER_MINUTE; if
		 * (System.currentTimeMillis() < startTime) { return startTime; } else {
		 * return
		 * ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime();
		 * } } else { return
		 * ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime();
		 * }
		 */

		if (!ServerState.getInstance().isOpenServer()
				|| (countryNpc != null && !countryNpc.getLifeStats().isAlreadyDead())) {
			return 0L;
		} else if (reliveTime != 0 && reliveTime >= System.currentTimeMillis()) {
			return reliveTime;
		} else {
			return ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime();
		}
	}
}
