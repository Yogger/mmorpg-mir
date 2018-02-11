package com.mmorpg.mir.model.commonactivity.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.event.IdentifyTreasureRankEvent;
import com.mmorpg.mir.model.commonactivity.model.CommonCheapGiftBag;
import com.mmorpg.mir.model.commonactivity.model.CommonConsumeActive;
import com.mmorpg.mir.model.commonactivity.model.CommonConsumeGift;
import com.mmorpg.mir.model.commonactivity.model.CommonFirstPay;
import com.mmorpg.mir.model.commonactivity.model.CommonGoldTreasury;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasure;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureTotalServers;
import com.mmorpg.mir.model.commonactivity.model.CommonLoginGift;
import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShop;
import com.mmorpg.mir.model.commonactivity.model.CommonRechargeCelebrate;
import com.mmorpg.mir.model.commonactivity.model.CommonRedPack;
import com.mmorpg.mir.model.commonactivity.model.CommonTreasureActive;
import com.mmorpg.mir.model.commonactivity.model.LuckyDraw;
import com.mmorpg.mir.model.commonactivity.model.ServerGoldTreasuryLog;
import com.mmorpg.mir.model.commonactivity.packet.CM_Consume_Gift_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Reset;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Can_Recollect_Count;
import com.mmorpg.mir.model.commonactivity.packet.SM_CommonFirstPay_Send;
import com.mmorpg.mir.model.commonactivity.packet.SM_CommonMarcoShop_Query_Info;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Celebrate_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Cheap_Gift_Bag_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Consume_Draw_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Identify_Treasure_Change;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Identify_Treasure_Query;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Identify_Treasure_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Login_Gift_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Recharge_Draw;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_RedPackActive_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_SpecialBoss_Die;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_SpecialBoss_Refresh;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_TreasureActive_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Draw_Recollect_Rewards;
import com.mmorpg.mir.model.commonactivity.packet.SM_Get_Recollect;
import com.mmorpg.mir.model.commonactivity.packet.SM_Gold_Treasury_BroadCast;
import com.mmorpg.mir.model.commonactivity.packet.SM_Gold_Treasury_Query;
import com.mmorpg.mir.model.commonactivity.packet.SM_Gold_Treasury_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Lucky_Draw_Draw;
import com.mmorpg.mir.model.commonactivity.packet.SM_Lucky_Draw_Recharge;
import com.mmorpg.mir.model.commonactivity.packet.SM_Lucky_Draw_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Recollect_All;
import com.mmorpg.mir.model.commonactivity.packet.SM_WeekCri_buy;
import com.mmorpg.mir.model.commonactivity.resource.CommonBossResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonCheapGiftBagResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonCollectWordResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonConsumeActiveResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonConsumeGiftResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonDoubleExpResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonFireCelebrateResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonFirstPayResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonGoldTreasuryResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonIdentifyTreasureResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonLoginGiftResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRechargeActiveResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRecollectResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRedPacketResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonSPServerResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonTreasureActivityResource;
import com.mmorpg.mir.model.commonactivity.resource.LuckyDrawResource;
import com.mmorpg.mir.model.commonactivity.resource.WeekCriResource;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.BetweenTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.New;

@Component
public class CommonActivityManager {
	private static CommonActivityManager INSTANCE;

	@Autowired
	private CommonActivityConfig config;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@Autowired
	private SpawnManager spawnManager;

	@Autowired
	private CountryManager countryManager;

	private NonBlockingHashMap<String, VisibleObject> specialBoss = new NonBlockingHashMap<String, VisibleObject>();

	public static CommonActivityManager getInstance() {
		return INSTANCE;
	}

	@PostConstruct
	public void init() {
		INSTANCE = this;
		initSpecialBossTask();
		preNotice();
	}

	public boolean specialBossIsAlive(String bossId) {
		return specialBoss.containsKey(bossId);
	}

	public HashSet<String> getAliveBoss() {
		return new HashSet<String>(specialBoss.keySet());
	}

	private void initSpecialBossTask() {
		for (final Map.Entry<String, ArrayList<String>> entry : config.SPECIAL_BOSSES_SPAWN_TIME.getValue().entrySet()) {
			for (String cron : entry.getValue()) {
				simpleScheduler.schedule(new ScheduledTask() {

					@Override
					public void run() {

						for (final CommonBossResource resource : config.bossStorage.getIndex(
								CommonBossResource.ACTIVITY_NAME_INDEX, entry.getKey())) {
							if (!resource.getBossTimeCondition().verify(null)) {
								continue;
							}

							if (specialBossIsAlive(resource.getId())) {
								continue;
							}

							final Npc boss = (Npc) spawnManager.creatObject(resource.getId(), 1);
							specialBoss.put(resource.getId(), boss);
							boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
								@Override
								public void die(Creature creature) {
									specialBoss.remove(resource.getId());
									Map<Integer, Player> ranks = boss.getDamageRank();
									for (Entry<Integer, Player> rank : ranks.entrySet()) {
										if (!resource.getRewardCondition().verify(rank.getValue(), false)) {
											continue;
										}
										// 构建奖励,邮件
										List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(
												rank.getKey(), resource.getRewardChoserId());
										Reward reward = RewardManager.getInstance().creatReward(rank.getValue(),
												rewardIds, null);
										String title = ChooserManager
												.getInstance()
												.chooseValueByRequire(rank.getKey(),
														resource.getRewardMailTitleChoserId()).get(0);
										String context = ChooserManager
												.getInstance()
												.chooseValueByRequire(rank.getKey(),
														resource.getRewardMailContentChoserId()).get(0);
										I18nUtils titel18n = I18nUtils.valueOf(title);
										I18nUtils contextl18n = I18nUtils.valueOf(context);
										contextl18n.addParm("BOSS",
												I18nPack.valueOf(boss.getObjectResource().getName()));
										titel18n.addParm("BOSS", I18nPack.valueOf(boss.getObjectResource().getName()));
										Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
										MailManager.getInstance().sendMail(mail, rank.getValue().getObjectId());
									}
									I18nUtils utils = I18nUtils.valueOf(resource.getAttackIl18nId());
									Player attacker = null;
									if (creature instanceof Summon) {
										Summon summon = (Summon) creature;
										attacker = summon.getMaster();
									} else {
										attacker = (Player) creature;
									}
									utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(attacker.getName()));
									utils.addParm(I18NparamKey.COUNTRY,
											I18nPack.valueOf(attacker.getCountry().getName()));
									utils.addParm(I18NparamKey.BOSS, I18nPack.valueOf(boss.getName()));
									ChatManager.getInstance().sendSystem(
											resource.getAttackChannel(),
											utils,
											null,
											CountryManager.getInstance().getCountryById(
													CountryId.valueOf(resource.getCountryId())));
									CountryManager.getInstance()
											.getCountryById(CountryId.valueOf(resource.getCountryId()))
											.sendPackAll(SM_Common_SpecialBoss_Die.valueOf(resource.getId()));
								}
							});
							spawnManager.bringIntoWorld(boss, 1);
							CountryManager.getInstance().getCountryById(CountryId.valueOf(resource.getCountryId()))
									.sendPackAll(SM_Common_SpecialBoss_Refresh.valueOf(resource.getId()));
						}

					}

					@Override
					public String getName() {
						return "绝世BOSS刷新";
					}
				}, cron);

			}
		}
	}

	private void preNotice() {
		for (final Map.Entry<String, ArrayList<String>> entry : config.SPECIAL_BOSS_REFRESH_NOTICE_TIME.getValue()
				.entrySet()) {
			for (String cron : entry.getValue()) {
				simpleScheduler.schedule(new ScheduledTask() {

					@Override
					public void run() {
						for (CommonBossResource resource : config.bossStorage.getIndex(
								CommonBossResource.ACTIVITY_NAME_INDEX, entry.getKey())) {
							if (!resource.getBossTimeCondition().verify(null)) {
								continue;
							}

							if (specialBossIsAlive(resource.getId())) {
								continue;
							}

							// notice
							I18nUtils content = I18nUtils.valueOf(resource.getNoticeIl18nId());
							ChatManager.getInstance().sendSystem(
									resource.getNoticeChannel(),
									content,
									null,
									CountryManager.getInstance().getCountryById(
											CountryId.valueOf(resource.getCountryId())));
						}

					}

					@Override
					public String getName() {
						return "提前5分钟刷新公告";
					}
				}, cron);
			}
		}

		for (final CommonDoubleExpResource resource : config.commonDBStorage.getAll()) {
			if (resource.getNoticeCron() == null || StringUtils.isBlank(resource.getNoticeCron())) {
				continue;
			}
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (resource.getNoticeConditions().verify(null)) {
						for (Map.Entry<String, Integer> entry : resource.getBeginNotice().entrySet()) {
							I18nUtils charI18n = I18nUtils.valueOf(entry.getKey());
							for (Country c : CountryManager.getInstance().getCountries().values()) {
								ChatManager.getInstance().sendSystem(entry.getValue(), charI18n, null, c);
							}
						}
					}
				}

				@Override
				public String getName() {
					return "双倍经验开始广播";
				}

			}, resource.getNoticeCron());
		}

	}

	public ArrayList<String> getConsumeCanRecievesReward(Player player, String activityName) {
		ArrayList<String> canRecieves = New.arrayList();

		for (CommonConsumeActiveResource resource : config.consumeStorages.getIndex(
				CommonConsumeActiveResource.ACTVITY_NAME, activityName)) {
			boolean rewarded = player.getCommonActivityPool().getConsumeActives().get(activityName).getRewarded()
					.contains(resource.getId());
			if (!rewarded && resource.getRecieveConditions().verify(player)) {
				canRecieves.add(resource.getId());
			}

		}
		return canRecieves;
	}

	public void loginCompensateMail(Player player) {
		Map<String, CommonConsumeActive> consumeActives = player.getCommonActivityPool().getConsumeActives();
		for (CommonConsumeActive consumeActive : consumeActives.values()) {
			for (CommonConsumeActiveResource resource : config.consumeStorages.getIndex(
					CommonConsumeActiveResource.ACTVITY_NAME, consumeActive.getActivityName())) {
				boolean rewarded = player.getCommonActivityPool().getConsumeActives()
						.get(consumeActive.getActivityName()).getRewarded().contains(resource.getId());
				if (!rewarded && resource.getMailCompensateConditions().verify(player)) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							resource.getChooserGroupId());
					Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
					Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailI18nTitle()),
							I18nUtils.valueOf(resource.getMailI18nContent()), null, reward);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					consumeActive.getRewarded().add(resource.getId());
				}
			}
		}
	}

	public void mailCompensateAll() {
		HashSet<RankType> types = New.hashSet();
		for (CommonConsumeActiveResource resource : CommonActivityConfig.getInstance().consumeStorages.getAll()) {
			BetweenTimeCondition cond = resource.getMailCompensateConditions().findConditionType(
					BetweenTimeCondition.class);
			if (cond == null) {
				continue;
			}
			cond.setThrowException(false);
			if (cond.verify(null)) {
				types.add(RankType.valueOf(resource.getRankType()));
			}
		}
		for (RankType type : types) {
			for (RankRow row : WorldRankManager.getInstance().getRankRowsCopy(type)) {
				Player player = PlayerManager.getInstance().getPlayer(row.getObjId());
				loginCompensateMail(player);
			}
		}
	}

	public void drawConsumeReward(Player player, String id) {
		CommonConsumeActiveResource resource = config.consumeStorages.get(id, true);
		CommonConsumeActive consumeActive = player.getCommonActivityPool().getConsumeActives()
				.get(resource.getActivityName());
		if (consumeActive.getRewarded().contains(id)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.CONSUME_ACTIVITY_REWARDED);
		}

		if (!resource.getRecieveConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
		if (!RewardManager.getInstance().playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_CONSUME_REWARD, id));
		consumeActive.getRewarded().add(resource.getId());

		PacketSendUtility.sendPacket(player, SM_Common_Consume_Draw_Reward.valueOf(id));
	}

	public void rewardCommonRecharge(Player player) {
		for (CommonRechargeActiveResource resource : config.rechargeStorages.getAll()) {
			CoreConditions conds = CoreConditionManager.getInstance().getCoreConditions(1, resource.getCompentConds());
			if (!conds.verify(player)) {
				continue;
			}
			CommonRechargeCelebrate recharge = player.getCommonActivityPool().getRechargeActives()
					.get(resource.getActiveName());
			if (recharge.getRewardedId() == null && recharge.isRechargeEnough()) {
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
						resource.getRewardGroupId());
				Reward reward = rewardManager.creatReward(player, rewardIds, null);
				I18nUtils titel18n = I18nUtils.valueOf(resource.getRewardTitleIl18nId());
				I18nUtils contextl18n = I18nUtils.valueOf(resource.getRewardContentIl18nId());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				recharge.setRewardedId(resource.getRewardGroupId());
			}

		}

	}

	public void drawCommonRechargeReward(Player player, String activityName) {
		CommonRechargeActiveResource resource = config.rechargeStorages.getUnique(
				CommonRechargeActiveResource.ACTIVE_NAME_INDEX, activityName);
		CoreConditions timeConds = CoreConditionManager.getInstance().getCoreConditions(1,
				resource.getActivityTimeConds());
		if (!timeConds.verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}

		CommonRechargeCelebrate recharge = player.getCommonActivityPool().getRechargeActives().get(activityName);
		if (!recharge.isRechargeEnough()) {
			throw new ManagedException(ManagedErrorCode.RECHARGE_NOT_ENOUGH);
		}

		if (recharge.getRewardedId() != null && recharge.getRewardedId().equals(resource.getRewardGroupId())) {
			throw new ManagedException(ManagedErrorCode.RECHARGE_ALREADY_RECIEVED);
		}

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, resource.getRewardGroupId());

		Reward reward = rewardManager.creatReward(player, rewardIds);

		rewardManager.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE,
				SubModuleType.COMMONACTIVITY_RECHARGE_REWARD, activityName));
		recharge.setRewardedId(resource.getRewardGroupId());
		PacketSendUtility.sendPacket(player, SM_Common_Recharge_Draw.valueOf(activityName, reward));
	}

	public void drawCommonLoginGift(Player player, String id) {
		CommonLoginGiftResource loginGiftResource = config.loginGiftStorage.get(id, true);
		CommonLoginGift loginGift = player.getCommonActivityPool().getCommonLoginActives()
				.get(loginGiftResource.getActiveName());
		// 判断领取条件
		if (!loginGiftResource.getCoreConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}
		// 判断是否领取
		if (loginGift.hasDrawBefore(id)) {
			throw new ManagedException(ManagedErrorCode.COMMON_LOGIN_GIFT_HAS_DRAW);
		}
		// 发放奖励
		RewardManager.getInstance().grantReward(player, loginGiftResource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_LOGIN_GIFT_REWARD, id));
		loginGift.addDrawLog(id);
		PacketSendUtility.sendPacket(player, SM_Common_Login_Gift_Reward.valueOf(id));
	}

	public void drawCommonCheapGiftBag(Player player, String id) {
		CommonCheapGiftBagResource cheapGiftBagResource = config.cheapGiftBagStorage.get(id, true);
		CommonCheapGiftBag cheapGiftBag = player.getCommonActivityPool().getCommonCheapActives()
				.get(cheapGiftBagResource.getActiveName());
		// 判断是否已经购买
		if (cheapGiftBag.hasBuyBefore(cheapGiftBagResource, config.cheapGiftBagStorage)) {
			throw new ManagedException(ManagedErrorCode.COMMON_CHEAP_GIFT_BAG_HAS_BUY);
		}
		// 判断是否购买了上一级别
		if (!cheapGiftBag.hasBuyLowLevel(cheapGiftBagResource, config.cheapGiftBagStorage)) {
			throw new ManagedException(ManagedErrorCode.COMMON_CHEAP_GIFT_BAG_NOT_BUY_LOW_LEVEL);
		}
		// 判断是否满足购买条件
		if (!cheapGiftBagResource.getCoreConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.COMMON_CHEAP_GIFT_NO_CONDITION);
		}
		// 判断消耗是否充足
		if (!cheapGiftBagResource.getCoreActions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// 开始购买
		cheapGiftBagResource.getCoreActions().act(player,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_CHEAP_GIFT_BAG_ACT, id));
		// 开始发奖并记录
		RewardManager.getInstance().grantReward(player, Arrays.asList(cheapGiftBagResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_CHEAP_GIFT_BAG_REWARD, id));
		cheapGiftBag.addLog(cheapGiftBagResource);
		// 返回购买
		PacketSendUtility.sendPacket(player, SM_Common_Cheap_Gift_Bag_Reward.valueOf(id));
	}

	public void rewardCelebrateFirework(Player player, String id, int count) {
		CommonFireCelebrateResource resource = config.fireStorages.get(id, true);

		if (!resource.getAttendCondition().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CoreActions coreActions = resource.getActions(count);
		if (!coreActions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		coreActions.act(
				player,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_CELEBRATE_FIREWORK_ACT, id
						+ "#" + count));

		Reward reward = rewardManager.creatReward(player, Arrays.asList(resource.getRewardIds()));
		reward.mutipleRewards(count);

		rewardManager.grantReward(
				player,
				reward,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_CELEBRATE_FIREWORK_REWARD, id
						+ "#" + count));
		PacketSendUtility.sendPacket(player, SM_Common_Celebrate_Reward.valueOf(id));
	}

	public void notifyAllPalyerCommonIdentifyTreasureServerChange(String activeName) {
		CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
				.getCommonIdentifyTreasureTotalServers();
		CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(activeName);
		SM_Common_Identify_Treasure_Change sm = SM_Common_Identify_Treasure_Change.valueOf(activeName, treasureServer);
		for (Country country : countryManager.getCountries().values()) {
			for (Player player : country.getCivils().values()) {
				if (player.getCommonActivityPool().getIdentifyTreasures().get(activeName) == null) {
					player.getCommonActivityPool().getIdentifyTreasures()
							.put(activeName, CommonIdentifyTreasure.valueOf(treasureServer.getVersion()));
				}
				if (treasureServer.getVersion() != player.getCommonActivityPool().getIdentifyTreasures()
						.get(activeName).getVersion()) {
					player.getCommonActivityPool().getIdentifyTreasures().get(activeName).reset(treasureServer);
				}
				PacketSendUtility.sendPacket(player, sm);
			}
		}
	}

	public void drawCommonIdentifyTreasureReward(Player player, SM_Common_Identify_Treasure_Reward sm) {
		CommonIdentifyTreasureResource treasureResource = config.treasureStorage.getUnique(
				CommonIdentifyTreasureResource.ACTIVE_NAME, sm.getActiveName());
		// 判断活动是否开启， 这里是针对自动开启的活动
		CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
				.getCommonIdentifyTreasureTotalServers();
		CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(sm
				.getActiveName());
		if (treasureServer == null) {
			synchronized (treasureTotalServers) {
				treasureServer = treasureTotalServers.getTreasureServerByActiveName(sm.getActiveName());
				if (treasureServer == null) {
					if (!treasureResource.getCoreOpenConditions().verify(player)) {
						throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_NOT_OPENING);
					}
					treasureServer = CommonIdentifyTreasureServer.valueOf(sm.getActiveName());
					treasureTotalServers.addTreasureServer(sm.getActiveName(), treasureServer);
				}
			}
		}
		// 添加用户的记录
		/*
		 * 1. 其实这里也可以不用控制多线程， 因为底层每个用户所有的操作都是在同一个线程中
		 */
		Map<String, CommonIdentifyTreasure> identifyTreasures = player.getCommonActivityPool().getIdentifyTreasures();
		CommonIdentifyTreasure identifyTreasure = identifyTreasures.get(sm.getActiveName());
		if (identifyTreasure == null) {
			synchronized (identifyTreasures) {
				identifyTreasure = identifyTreasures.get(sm.getActiveName());
				if (identifyTreasure == null) {
					identifyTreasure = CommonIdentifyTreasure.valueOf(treasureServer.getVersion());
					identifyTreasures.put(sm.getActiveName(), identifyTreasure);
				}
			}
		}
		String rewardId = null;
		boolean isBigTreasure = false;
		// 判断鉴宝条件
		if (!treasureResource.getCoreConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.COMMON_IDENTITY_TREASURE_NOT_CONDITION);
		}
		// 判断消耗
		if (!treasureResource.getCoreActions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
		}
		// 判断背包
		if (player.getPack().getEmptySize() < 1) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}
		// 鉴宝
		treasureResource.getCoreActions().act(
				player,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_IDENTIFY_TREASURE,
						sm.getActiveName()));
		if (identifyTreasure.getLuckValue() >= treasureResource.getMaxLuckValue() - treasureResource.getPerLuckValue()) {
			rewardId = treasureResource.getTreasureRewardId();
		} else {
			rewardId = ChooserManager.getInstance().chooseValueByRequire(player, treasureResource.getChooserGroupId())
					.get(0);
		}
		// 发放奖励
		identifyTreasure.addLuckValue(treasureResource.getPerLuckValue());
		if (rewardId.equals(treasureResource.getTreasureRewardId())) {// 大奖
			identifyTreasure.clearLuckValue();
			isBigTreasure = true;
		}
		Reward reward = rewardManager.grantReward(
				player,
				Arrays.asList(rewardId),
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_IDENTIFY_TREASURE,
						sm.getActiveName()));
		// 推送前段
		sm.setActiveName(sm.getActiveName()).setCurrentLuckValue(identifyTreasure.getLuckValue()).setItemId(rewardId);
		List<RewardItem> rewardItems = reward.getItemsByType(RewardType.ITEM);
		for (RewardItem rewardItem : rewardItems) {
			ItemResource itemResource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (isBigTreasure) {
				// 推送全服广播
				I18nUtils tvI18n = I18nUtils.valueOf(treasureResource.getTvI18nId());
				tvI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				tvI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ItemShow itemShow = new ItemShow();
				itemShow.setKey(rewardItem.getCode());
				itemShow.setOwner(player.getName());
				itemShow.setItem(player.getPack().getItemByKey(rewardItem.getCode()));
				tvI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(treasureResource.getTvChannel(), tvI18n, null);

				// 推送聊天广播
				I18nUtils charI18n = I18nUtils.valueOf(treasureResource.getChartI18nId());
				charI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				charI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				charI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(treasureResource.getChartChannel(), charI18n, null);
			}
			// 触发排行榜事件
			if (itemResource.getQuality() >= treasureResource.getQuality()) {
				EventBusManager.getInstance().submit(
						IdentifyTreasureRankEvent.valueOf(player.getObjectId(), sm.getActiveName(), player.getName(),
								rewardId, isBigTreasure));
			}
		}
	}

	// 暴击活动
	public void buyCriItem(Player player, int count, int sign) {
		if (!config.getWeekCriActivityTimeCond().verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}
		int openCount = ServerState.getInstance().getServerEnt().getWeekCriOpenCount();
		if (openCount == -1) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		int index = openCount % 3;

		WeekCriResource resource = config.weekCriStorage.get(index, true);
		CoreActions coreActions = CoreActionManager.getInstance().getCoreActions(count, resource.getActionId());
		if (!coreActions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		Reward reward = RewardManager.getInstance().creatReward(player, resource.getRewardId(), null);
		reward.mutipleRewards(count);
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.WEEK_CRI, SubModuleType.WEEK_CRI_REWARD, index + "#" + count));
		coreActions.act(player,
				ModuleInfo.valueOf(ModuleType.WEEK_CRI, SubModuleType.WEEK_CRI_ACT, index + "#" + count));
		PacketSendUtility.sendPacket(player, SM_WeekCri_buy.valueOf(sign));
	}

	public void queryCommonIdentifyTreasure(Player player, String activeName) {
		CommonIdentifyTreasureTotalServers treasureTotalServers = ServerState.getInstance()
				.getCommonIdentifyTreasureTotalServers();
		CommonIdentifyTreasureServer treasureServer = treasureTotalServers.getTreasureServerByActiveName(activeName);
		if (treasureServer == null) {
			synchronized (treasureTotalServers) {
				treasureServer = treasureTotalServers.getTreasureServerByActiveName(activeName);
				if (treasureServer == null) {
					treasureServer = CommonIdentifyTreasureServer.valueOf(activeName);
					treasureTotalServers.addTreasureServer(activeName, treasureServer);
				}
			}
		}
		PacketSendUtility.sendPacket(player,
				SM_Common_Identify_Treasure_Query.valueOf(activeName, treasureServer.queryIdentifyTreasureRank()));
	}

	public void drawCommonFirstPayReward(Player player, String id) {
		CommonFirstPayResource firstPayResource = config.firstPayStorage.get(id, true);
		String activeName = firstPayResource.getActiveName();
		CommonFirstPay firstPay = player.getCommonActivityPool().getFirstPays().get(activeName);
		// 判断领取条件
		if (!firstPayResource.getCoreConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.COMMON_FIRST_PAY_NOT_CONDITION);
		}
		if (firstPay.hasDrawBefore(id)) {
			throw new ManagedException(ManagedErrorCode.COMMON_FIRST_PAY_HAS_DRAW);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				firstPayResource.getChoosergroupId());
		// 发放奖励
		rewardManager
				.grantReward(player, rewardIds, ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE,
						SubModuleType.COMMONACTIVITY_FIRST_PAY_REWARD, activeName));
		// 添加记录
		firstPay.addRewardLog(id);
	}

	public void addFirstPayEndMail(Player player) {
		Map<String, CommonFirstPay> firstPays = player.getCommonActivityPool().getFirstPays();
		for (String activeName : firstPays.keySet()) {
			List<CommonFirstPayResource> firstPayResources = config.firstPayStorage.getIndex(
					CommonFirstPayResource.ACTIVE_NAME, activeName);
			for (CommonFirstPayResource firstPay : firstPayResources) {
				if (firstPays.get(activeName).getRewardIds().contains(firstPay.getId())) {
					continue;
				}
				if (!firstPay.getEndCoreConditions().verify(player)) {
					continue;
				}
				if (firstPay.getCoreConditions().verify(player)) {
					continue;
				}
				// 邮件发送奖励
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
						firstPay.getChoosergroupId());
				Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
				I18nUtils titel18n = I18nUtils.valueOf(firstPay.getI18nTitle());
				I18nUtils contextl18n = I18nUtils.valueOf(firstPay.getI18nContent());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				// 记录日志
				firstPays.get(activeName).addRewardLog(firstPay.getId());
			}
		}
	}

	public void addFirstPaySend(Player player, String activeName) {
		PacketSendUtility.sendPacket(
				player,
				SM_CommonFirstPay_Send.valueOf(activeName, player.getCommonActivityPool().getFirstPays()
						.get(activeName).getPayCount()));
	}

	public void queryMarcoShop(Player player, String activityName) {
		CommonMarcoShopResource resource = CommonActivityConfig.getInstance().commonShopStorage.getUnique(
				CommonMarcoShopResource.ACTIVITY_NAME, activityName);
		if (!resource.getOpenConds().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		CommonMarcoShop marcoShop = player.getCommonActivityPool().getCommonMarcoShop().get(activityName);
		if (marcoShop.checkCanRefresh()) {
			marcoShop.refresh(player, true, false);
		}
		PacketSendUtility.sendPacket(player, SM_CommonMarcoShop_Query_Info.valueOf(player, activityName));
	}

	public void customRefresh(Player player, String activityName) {
		CommonMarcoShopResource resource = CommonActivityConfig.getInstance().commonShopStorage.getUnique(
				CommonMarcoShopResource.ACTIVITY_NAME, activityName);
		if (!resource.getOpenConds().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		CommonMarcoShop marcoShop = player.getCommonActivityPool().getCommonMarcoShop().get(activityName);
		int itemCount = 0;
		if (resource.getPriItemAction() != null && resource.getPriItemAction().length != 0) {
			// 暴击丹
			String criItemId = CoreActionManager.getInstance().getCoreActions(1, resource.getPriItemAction())
					.getFirstItemKey();
			itemCount = (int) player.getPack().getItemSizeByKey(criItemId);
		}
		CoreActions coreActions = new CoreActions();
		if (resource.getActCount() <= itemCount) {
			// 优先扣除道具
			coreActions = CoreActionManager.getInstance().getCoreActions(resource.getActCount(),
					resource.getPriItemAction());
			coreActions.verify(player);
			coreActions
					.act(player,
							ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.MARCOSHOP_REFRESH_ACT,
									resource.getId()));
		} else {
			if (itemCount > 0) {
				coreActions.addActions(CoreActionManager.getInstance().getCoreActions(itemCount,
						resource.getPriItemAction()));
			}
			int needGold = resource.getActCount() - itemCount;
			// 扣除元宝 CoerActions
			coreActions.addActions(CoreActionManager.getInstance().getCoreActions(needGold, resource.getGoldAction()));
			if (!coreActions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			coreActions
					.act(player,
							ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.MARCOSHOP_REFRESH_ACT,
									resource.getId()));
		}

		marcoShop.refresh(player, false, true);
	}

	public void systemRefresh(Player player, String activityName) {
		CommonMarcoShopResource resource = CommonActivityConfig.getInstance().commonShopStorage.getUnique(
				CommonMarcoShopResource.ACTIVITY_NAME, activityName);
		if (!resource.getOpenConds().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		CommonMarcoShop marcoShop = player.getCommonActivityPool().getCommonMarcoShop().get(activityName);
		if (!marcoShop.checkCanRefresh()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		marcoShop.refresh(player, true, true);
	}

	public void buy(Player player, String activityName, int gridIndex) {
		CommonMarcoShopResource resource = CommonActivityConfig.getInstance().commonShopStorage.getUnique(
				CommonMarcoShopResource.ACTIVITY_NAME, activityName);
		if (!resource.getOpenConds().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		int gridCountMax = resource.getGridCount();
		if (gridIndex < 0 || gridIndex >= gridCountMax) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CommonMarcoShop marcoShop = player.getCommonActivityPool().getCommonMarcoShop().get(activityName);
		marcoShop.buy(player, gridIndex);
	}

	public void rewardTreasureActivity(Player player, String id) {
		CommonTreasureActivityResource resource = CommonActivityConfig.getInstance().commonTreasureStorage
				.get(id, true);
		CoreConditions timeConds = CommonActivityConfig.getInstance().getTreasureActiveTimeConds(
				resource.getActivityName());
		if (!timeConds.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		CommonTreasureActive active = player.getCommonActivityPool().getTreasurueActives()
				.get(resource.getActivityName());
		if (active.isRewarded(id)) {
			throw new ManagedException(ManagedErrorCode.COMMON_TREASURE_ACTIVE_REWARDED);
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getRewardChoserId());
		RewardManager.getInstance().grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_TREASUREACTIVE_REWARD, id));
		active.rewarded(id);
		PacketSendUtility.sendPacket(player, SM_Common_TreasureActive_Reward.valueOf(id));
	}

	public void compensateMailReward(Player player) {
		Map<String, CommonTreasureActive> treasureActives = player.getCommonActivityPool().getTreasurueActives();
		for (CommonTreasureActive active : treasureActives.values()) {
			for (CommonTreasureActivityResource resource : config.commonTreasureStorage.getIndex(
					CommonTreasureActivityResource.ACTIVITY_NAME, active.getActivityName())) {
				boolean mailVerified = resource.getMailConditions().verify(player);
				boolean notRewarded = !active.isRewarded(resource.getId());
				if (mailVerified && notRewarded) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							resource.getRewardChoserId());
					Reward reward = rewardManager.creatReward(player, rewardIds, null);
					Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getTitleIl18n()),
							I18nUtils.valueOf(resource.getContentIl18n()), null, reward);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					active.rewarded(resource.getId());
				}
			}
		}
	}

	public void rewardRedPacket(Player player, String id) {
		CommonRedPacketResource resource = CommonActivityConfig.getInstance().redPacketStorage.get(id, true);

		CoreConditions timeConds = CommonActivityConfig.getInstance().getRedPacketTimeConds(resource.getActivityName());
		if (!timeConds.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		if (!resource.getRewardConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// if (!resource.getRewardConditions().verify(player, true)) {
		// throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		// }

		CommonRedPack redPack = player.getCommonActivityPool().getRedPacketActives().get(resource.getActivityName());
		redPack.refresh();
		if (redPack.isRewarded(id)) {
			throw new ManagedException(ManagedErrorCode.COMMON_REDPACK_ACTIVE_REWARDED);
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		RewardManager.getInstance().grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_REDPACK_REWARD, id));
		redPack.rewarded(id);
		PacketSendUtility.sendPacket(player, SM_Common_RedPackActive_Reward.valueOf(id));
	}

	public void drawCommonCollectWordActiveReward(Player player, String id, int count) {
		CommonCollectWordResource collectWordResource = config.collectWordStorage.get(id, true);

		if (!collectWordResource.getCoreConditions(count).verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.COMMON_COLLECT_WORLD_NOT_CONDITION);
		}
		if (!collectWordResource.getCoreActions(count).verify(player)) {
			throw new ManagedException(ManagedErrorCode.COMMON_COLLECT_WORLD_NO_CORE);
		}
		collectWordResource.getCoreActions(count).act(
				player,
				ModuleInfo
						.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMON_ACTIVE_COLLECT_WORD, id + "#" + count));
		String[] rewardIds = new String[count];
		Arrays.fill(rewardIds, collectWordResource.getRewardId());
		rewardManager.grantReward(player, Arrays.asList(rewardIds), ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE,
				SubModuleType.COMMON_ACTIVE_COLLECT_WORD, id + "#" + count));
	}

	public void getRecollectStatus(Player player) {
		PacketSendUtility.sendPacket(player, SM_Get_Recollect.valueOf(player));
	}

	public void recieveRecollectRewards(Player player, HashSet<String> resourceId, boolean useGold) {
		CoreConditions timeConditions = new CoreConditions();
		timeConditions.addCondition(CommonActivityConfig.getInstance().getTimeCondition());
		if (!timeConditions.verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.CLAW_NOT_PERIOD_TIME);
		}

		CoreActions actions = new CoreActions();
		Reward reward = Reward.valueOf();
		for (String id : resourceId) {
			CommonRecollectResource resource = CommonActivityConfig.getInstance().recollectResources.get(id, true);
			if (!resource.getClawbackConditions().verify(player, false)) {
				continue;
			}
			int count = player.getCommonActivityPool().getCurrentRecollectActive()
					.getCanClawbackCount(player, resource.getEventType());
			if (useGold && count > 0) {
				actions.addActions(CommonActivityConfig.getInstance().getRecollectGoldActions(resource.getEventType(),
						count));
				reward.addReward(CommonActivityConfig.getInstance().getRecollectRewards(player,
						resource.getEventType(), count));
			} else if (count > 0) {
				actions.addActions(CommonActivityConfig.getInstance().getRecollectCopperActions(
						resource.getEventType(), count));
				Reward addReward = CommonActivityConfig.getInstance().getRecollectRewards(player,
						resource.getEventType(), count);
				addReward.divideIntoTwoPieces(0.30d, new RewardType[] { RewardType.EXP, RewardType.CURRENCY,
						RewardType.ITEM });
				reward.addReward(addReward);
			}
		}

		if (actions.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.NO_CLAW_ITEMS);
		}
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.ACTIVITY_RECOLLECT, SubModuleType.ACTIVITY_RECOLLECT_ACTIONS));
		for (String id : resourceId) {
			CommonRecollectResource resource = CommonActivityConfig.getInstance().recollectResources.get(id, true);
			player.getCommonActivityPool().getCurrentRecollectActive().recollectAll(player, resource.getEventType());
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.ACTIVITY_RECOLLECT, SubModuleType.ACTIVITY_RECOLLECT_REWARD));
		PacketSendUtility.sendPacket(player, SM_Draw_Recollect_Rewards.valueOf(resourceId));
		PacketSendUtility.sendPacket(player, SM_Can_Recollect_Count.valueOf(player));
	}

	public void recieveRecollectRewardsOneKey(Player player, boolean useGold) {
		CoreConditions timeConditions = new CoreConditions();
		timeConditions.addCondition(CommonActivityConfig.getInstance().getTimeCondition());
		if (!timeConditions.verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.CLAW_NOT_PERIOD_TIME);
		}
		CoreActions actions = new CoreActions();
		Reward reward = Reward.valueOf();
		CoreActions compareActions = new CoreActions();
		HashSet<String> successIds = New.hashSet();
		for (CommonRecollectResource resource : CommonActivityConfig.getInstance().getCurrentCommonRecollectResources()) {
			if (!resource.getClawbackConditions().verify(player, false)) {
				continue;
			}
			int count = player.getCommonActivityPool().getCurrentRecollectActive()
					.getCanClawbackCount(player, resource.getEventType());
			if (useGold && count > 0) {
				CoreActions typeActions = CommonActivityConfig.getInstance().getRecollectGoldActions(
						resource.getEventType(), count);
				compareActions.addActions(typeActions);
				if (compareActions.verify(player, false)) {
					successIds.add(resource.getId());
					actions.addActions(typeActions);
					Reward addReward = CommonActivityConfig.getInstance().getRecollectRewards(player,
							resource.getEventType(), count);
					reward.addReward(addReward);
				} else {
					if (actions.isEmpty()) {
						throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
					}
					break;
				}
			} else if (count > 0) {
				CoreActions typeActions = CommonActivityConfig.getInstance().getRecollectCopperActions(
						resource.getEventType(), count);
				compareActions.addActions(typeActions);
				if (compareActions.verify(player, false)) {
					successIds.add(resource.getId());
					actions.addActions(typeActions);
					Reward addReward = CommonActivityConfig.getInstance().getRecollectRewards(player,
							resource.getEventType(), count);
					addReward.divideIntoTwoPieces(0.30d, new RewardType[] { RewardType.EXP, RewardType.CURRENCY,
							RewardType.ITEM });
					reward.addReward(addReward);
				} else {
					if (actions.isEmpty()) {
						throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_COPPER);
					}
					break;
				}
			}
		}

		if (actions.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.NO_CLAW_ITEMS);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.ACTIVITY_RECOLLECT, SubModuleType.ACTIVITY_RECOLLECT_ACTIONS));
		for (String id : successIds) {
			CommonRecollectResource resource = CommonActivityConfig.getInstance().recollectResources.get(id, true);
			player.getCommonActivityPool().getCurrentRecollectActive().recollectAll(player, resource.getEventType());
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.ACTIVITY_RECOLLECT, SubModuleType.ACTIVITY_RECOLLECT_REWARD));
		PacketSendUtility.sendPacket(player, SM_Recollect_All.valueOf(successIds));
		PacketSendUtility.sendPacket(player, SM_Can_Recollect_Count.valueOf(player));
	}

	public void rewardSpecialServer(Player player) {
		CommonSPServerResource resource = config.getResourceContainName(ServerState.getInstance().getServerName());
		if (resource == null) {
			return;
		}

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserId());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
		Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getTitleId()), I18nUtils.valueOf(resource.getContentId()),
				null, reward);
		MailManager.getInstance().sendMail(mail, player.getObjectId());
	}

	public void luckyDraw(Player player) {
		LuckyDrawResource resource = config.luckyDrawStorage.getAll().toArray(new LuckyDrawResource[0])[0];
		// 判断条件
		resource.getCoreConditions().verify(player, true);
		// 判断消耗
		LuckyDraw luckyDraw = player.getCommonActivityPool().getLuckyDraw();
		if (!luckyDraw.canDraw(resource.getPreLuckyDrawRecharge())) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		// 抽奖
		String rewardId = ChooserManager.getInstance().chooseValueByRequire(player, resource.getChooserGroupId())
				.get(0);
		luckyDraw.addOneDraw(rewardId);
		PacketSendUtility.sendPacket(player, SM_Lucky_Draw_Draw.valueOf(rewardId));
	}

	public void luckyDrawReward(Player player) {
		LuckyDrawResource resource = config.luckyDrawStorage.getAll().toArray(new LuckyDrawResource[0])[0];
		// 判断条件
		resource.getCoreConditions().verify(player, true);
		LuckyDraw luckyDraw = player.getCommonActivityPool().getLuckyDraw();
		if (luckyDraw.getConcurrentRewardId() == null) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		// 发放奖励
		Reward reward = RewardManager.getInstance().creatReward(player, luckyDraw.getConcurrentRewardId(), null);
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.LUCKY_REWARD));
		luckyDraw.clearOneDraw();
		// 推送前段
		PacketSendUtility.sendPacket(player, new SM_Lucky_Draw_Reward());
		// 广播
		List<RewardItem> rewardItems = reward.getItemsByType(RewardType.ITEM);
		for (RewardItem rewardItem : rewardItems) {
			ItemResource itemResource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (itemResource.getQuality() >= resource.getQuality()) {
				// 推送全服广播
				I18nUtils tvI18n = I18nUtils.valueOf(resource.getTvI18nId());
				tvI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				tvI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ItemShow itemShow = new ItemShow();
				itemShow.setKey(rewardItem.getCode());
				itemShow.setOwner(player.getName());
				itemShow.setItem(player.getPack().getItemByKey(rewardItem.getCode()));
				tvI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(resource.getTvChannel(), tvI18n, null);

				// 推送聊天广播
				I18nUtils charI18n = I18nUtils.valueOf(resource.getChartI18nId());
				charI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				charI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				charI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(resource.getChartChannel(), charI18n, null);
			}
		}
	}

	public void addLuckyDrawSend(Player player) {
		LuckyDraw luckyDraw = player.getCommonActivityPool().getLuckyDraw();
		PacketSendUtility.sendPacket(player,
				SM_Lucky_Draw_Recharge.valueOf(luckyDraw.getPayCount(), luckyDraw.getDrawCount()));
	}

	public void goldTreasuryReward(Player player, CM_Gold_Treasury_Reward req) {
		SM_Gold_Treasury_Reward sm = SM_Gold_Treasury_Reward.valueOf(req);
		CommonGoldTreasuryResource treasuryResource = config.getGoldTreasuryResource(req.getActiveName(),
				req.getGroupId());
		CommonGoldTreasury treasury = player.getCommonActivityPool().getGoldTreasurys().get(req.getActiveName());
		if (!treasuryResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.GOLD_TREASURY_REWARD_NO_CONDITION);
		}
		if (treasury.hashDrwarded(req.getGroupId(), req.getIndex())) {
			throw new ManagedException(ManagedErrorCode.GOLD_TREASURY_REWARDED);
		}
		if (!treasuryResource.getCoreActions().verify(player, false)) {
			if (!treasuryResource.getGoldCoreActions().verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.GOLD_TREASURY_REWARD_NO_ACTION);
			}
			treasuryResource.getGoldCoreActions().act(
					player,
					ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE,
							config.getGoldTreasuryActionSubType(req.getGroupId(), true)));
			sm.setGoldAction(true);
		} else {
			treasuryResource.getCoreActions().act(
					player,
					ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE,
							config.getGoldTreasuryActionSubType(req.getGroupId(), true)));
			sm.setGoldAction(false);
		}
		String rewardId = ChooserManager
				.getInstance()
				.chooserFormExcept(player, treasuryResource.getChooserGroupId(),
						treasury.getGroupRewarded(req.getGroupId()), null).get(0);
		Reward reward = RewardManager.getInstance().grantReward(player, rewardId,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, config.getGoldTreasuryRewardSubType(req.getGroupId())));
		treasury.addLog(req.getGroupId(), req.getIndex(), rewardId);
		// 广播
		List<RewardItem> rewardItems = reward.getItemsByType(RewardType.ITEM);
		for (RewardItem rewardItem : rewardItems) {
			ItemResource itemResource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (itemResource.getQuality() >= treasuryResource.getQuality()) {
				// 推送全服广播
				I18nUtils tvI18n = I18nUtils.valueOf(treasuryResource.getTvI18nId());
				tvI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				tvI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ItemShow itemShow = new ItemShow();
				itemShow.setKey(rewardItem.getCode());
				itemShow.setOwner(player.getName());
				itemShow.setItem(player.getPack().getItemByKey(rewardItem.getCode()));
				tvI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(treasuryResource.getTvChannel(), tvI18n, null);

				// 推送聊天广播
				I18nUtils charI18n = I18nUtils.valueOf(treasuryResource.getChartI18nId());
				charI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				charI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				charI18n.addParm(I18NparamKey.ITEM, I18nPack.valueOf(itemShow));
				ChatManager.getInstance().sendSystem(treasuryResource.getChartChannel(), charI18n, null);

				// 大奖广播
				if (treasuryResource.getTreasureRewardId().equals(rewardId)) {
					ServerState.getInstance().getGoldTreasuryServers().get(req.getActiveName())
							.setLatestLog(ServerGoldTreasuryLog.valueOf(player.getName(), rewardId, req.getGroupId()));
					for (Country country : countryManager.getCountries().values()) {
						for (Player player2 : country.getCivils().values()) {
							PacketSendUtility.sendPacket(
									player2,
									SM_Gold_Treasury_BroadCast.valueOf(req.getActiveName(), player.getName(),
											req.getGroupId(), rewardId));
						}
					}
				}

				// 排行
				ServerState.getInstance().getGoldTreasuryServers().get(req.getActiveName())
						.addLog(player.getName(), rewardId, req.getGroupId());
			}
		}
		sm.setRewardId(rewardId);
		PacketSendUtility.sendPacket(player, sm);
	}

	public void goldTreasuryReset(Player player, CM_Gold_Treasury_Reset req) {
		CommonGoldTreasuryResource treasuryResource = config.getGoldTreasuryResource(req.getActiveName(),
				req.getGroupId());
		CommonGoldTreasury treasury = player.getCommonActivityPool().getGoldTreasurys().get(req.getActiveName());
		if (treasuryResource.getResetTimes() <= treasury.getResetTimes(req.getGroupId())) {
			throw new ManagedException(ManagedErrorCode.GOLD_TREASURY_RESET_NO_TIMES);
		}
		treasury.reset(req.getGroupId());
	}

	public void goldTreasuryQuery(Player player, String activeName) {
		LinkedList<ServerGoldTreasuryLog> logs = ServerState.getInstance().getGoldTreasuryServers().get(activeName)
				.getLogs();
		PacketSendUtility.sendPacket(player, SM_Gold_Treasury_Query.valueOf(activeName, logs));
	}

	public void goldTreasuryQueryMaster(Player player, String activeName) {
		ServerGoldTreasuryLog lastestLog = ServerState.getInstance().getGoldTreasuryServers().get(activeName)
				.getLatestLog();
		if (lastestLog != null) {
			PacketSendUtility.sendPacket(player, SM_Gold_Treasury_BroadCast.valueOf(activeName,
					lastestLog.getPlayerName(), lastestLog.getGroupId(), lastestLog.getItemId()));
		}

	}

	public void rewardConsumeGift(Player player, CM_Consume_Gift_Reward req) {
		List<CommonConsumeGiftResource> resources = config.consumeGiftStroage.getIndex(
				CommonConsumeGiftResource.ACTIVITY_NAME, req.getActiveName());
		CommonConsumeGiftResource consumeResource = null;
		for (CommonConsumeGiftResource resource : resources) {
			if (resource.getGroupId() == req.getGroupId()) {
				consumeResource = resource;
			}
		}
		if (consumeResource == null) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		CommonConsumeGift consumeGift = player.getCommonActivityPool().getConsumeGifts().get(req.getActiveName());
		// 判断领取条件
		if (!consumeResource.getCoreConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.CONSUME_GIFT_NO_CONDTION);
		}
		if (consumeGift.hasDrawBefore(req.getGroupId())) {
			throw new ManagedException(ManagedErrorCode.CONSUME_GIFT_HAS_REWARD);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				consumeResource.getChooserGroupId());
		// 发放奖励
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.COMMONACTIVITY_CONSUME_GIFT_REWARD));
		// 添加记录
		consumeGift.addRewardLog(req.getGroupId());
		// 广播
		I18nUtils tvI18n = I18nUtils.valueOf(consumeResource.getTvI18nId());
		tvI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
		tvI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(consumeResource.getTvChannel(), tvI18n, null);
		I18nUtils charI18n = I18nUtils.valueOf(consumeResource.getChartI18nId());
		charI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
		charI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(consumeResource.getChartChannel(), charI18n, null);
	}

	public void addConsumeGiftEndMail(Player player) {
		Map<String, CommonConsumeGift> consumeGifts = player.getCommonActivityPool().getConsumeGifts();
		for (String activeName : consumeGifts.keySet()) {
			List<CommonConsumeGiftResource> consumeGiftResources = config.consumeGiftStroage.getIndex(
					CommonConsumeGiftResource.ACTIVITY_NAME, activeName);
			for (CommonConsumeGiftResource giftResource : consumeGiftResources) {
				if (consumeGifts.get(activeName).hasDrawBefore(giftResource.getGroupId())) {
					continue;
				}
				if (!giftResource.getEndCoreConditions().verify(player)) {
					continue;
				}
				if (giftResource.getCoreConditions().verify(player)) {
					continue;
				}
				// 邮件发送奖励
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
						giftResource.getChooserGroupId());
				Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
				I18nUtils titel18n = I18nUtils.valueOf(giftResource.getI18nTitle());
				I18nUtils contextl18n = I18nUtils.valueOf(giftResource.getI18nContent());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				// 记录日志
				consumeGifts.get(activeName).addRewardLog(giftResource.getGroupId());
			}
		}
	}
}
