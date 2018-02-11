package com.mmorpg.mir.model.openactive.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.model.ActivityEnum;
import com.mmorpg.mir.model.openactive.model.ActivityInfo;
import com.mmorpg.mir.model.openactive.model.ArtifactActive;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;
import com.mmorpg.mir.model.openactive.model.EnhancePowerActive;
import com.mmorpg.mir.model.openactive.model.EquipActive;
import com.mmorpg.mir.model.openactive.model.ExpActive;
import com.mmorpg.mir.model.openactive.model.GiftActive;
import com.mmorpg.mir.model.openactive.model.GroupPurchase;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseThree;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseTwo;
import com.mmorpg.mir.model.openactive.model.HorseUpgradeActive;
import com.mmorpg.mir.model.openactive.model.OldSoulActive;
import com.mmorpg.mir.model.openactive.model.RechargeCelebrate;
import com.mmorpg.mir.model.openactive.model.SoulActive;
import com.mmorpg.mir.model.openactive.model.vo.CompeteVO;
import com.mmorpg.mir.model.openactive.model.vo.OldCompeteVO;
import com.mmorpg.mir.model.openactive.packet.SM_Celebrate_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_Draw_CelebrateReward;
import com.mmorpg.mir.model.openactive.packet.SM_Draw_CompeteReward;
import com.mmorpg.mir.model.openactive.packet.SM_Draw_OldCompeteReward;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseThreeInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseTwoInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Three_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Two_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_OldSevenCompete_Push;
import com.mmorpg.mir.model.openactive.packet.SM_Old_SoulUpgrade_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_OpenActive_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_Public_Test_Gift_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_SevenCompete_Push;
import com.mmorpg.mir.model.openactive.packet.SM_SpecialBoss_Die;
import com.mmorpg.mir.model.openactive.packet.SM_SpecialBoss_Refresh;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseResource;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseThreeResource;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseTwoResource;
import com.mmorpg.mir.model.openactive.resource.OldOpenActiveCompeteResource;
import com.mmorpg.mir.model.openactive.resource.OldOpenActiveSoulUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveArtifactResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveCollectItemResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveCompeteResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEnhanceResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEquipResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveExpResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveHorseUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveSoulUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.PublicTestGiftResource;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class OpenActiveManager {

	@Autowired
	private OpenActiveConfig config;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private MailManager mailManager;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@Autowired
	private SpawnManager spawnManager;

	private NonBlockingHashMap<CountryId, VisibleObject> countrySpeicalBoss = new NonBlockingHashMap<CountryId, VisibleObject>();

	private static OpenActiveManager INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
		initSpecialBossTask();
		preNotice();
	}

	private void initSpecialBossTask() {
		for (String cronTime : config.SPECIAL_BOSSES_SPAWN_TIME.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (!config.getPublicTestConditions().verify(null)
							&& !ServerState.getInstance().isCelebrateActivityOpen(ActivityEnum.BOSS)) {
						return;
					}

					for (Entry<String, String> entry : config.SPECIAL_BOSSES.getValue().entrySet()) {
						final CountryId countryId = CountryId.valueOf(Integer.valueOf(entry.getKey()));
						// 已经存在的
						if (specialBossIsAlive(countryId)) {
							continue;
						}
						// spawn
						final Npc boss = (Npc) spawnManager.creatObject(entry.getValue(), 1);
						countrySpeicalBoss.put(countryId, boss);
						boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
							@Override
							public void die(Creature creature) {
								countrySpeicalBoss.remove(countryId);
								Map<Integer, Player> ranks = boss.getDamageRank();
								for (Entry<Integer, Player> rank : ranks.entrySet()) {
									if (!OpenActiveConfig.getInstance().getSpecialBossRewardCondition().verify(rank.getValue(), false)) {
										continue;
									}
									// 构建奖励,邮件
									List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(
											rank.getKey(),
											config.SPECIAL_BOSS_REWARD.getValue().get(boss.getSpawnKey()));
									Reward reward = RewardManager.getInstance().creatReward(rank.getValue(), rewardIds,
											null);
									String title = ChooserManager
											.getInstance()
											.chooseValueByRequire(rank.getKey(),
													config.SPECIAL_BOSS_REWARD_MAIL_TITLE.getValue()).get(0);
									String context = ChooserManager
											.getInstance()
											.chooseValueByRequire(rank.getKey(),
													config.SPECIAL_BOSS_REWARD_MAIL_CONTENT.getValue()).get(0);
									I18nUtils titel18n = I18nUtils.valueOf(title);
									I18nUtils contextl18n = I18nUtils.valueOf(context);
									contextl18n.addParm("BOSS", I18nPack.valueOf(boss.getObjectResource().getName()));
									titel18n.addParm("BOSS", I18nPack.valueOf(boss.getObjectResource().getName()));
									Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
									MailManager.getInstance().sendMail(mail, rank.getValue().getObjectId());
								}
								I18nUtils utils = I18nUtils.valueOf("320002");
								Player attacker = null;
								if (creature instanceof Summon) {
									Summon summon = (Summon) creature;
									attacker = summon.getMaster();
								} else {
									attacker = (Player) creature;
								}
								utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(attacker.getName()));
								utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(attacker.getCountry().getName()));
								utils.addParm(I18NparamKey.BOSS, I18nPack.valueOf(boss.getName()));
								ChatManager.getInstance().sendSystem(6, utils, null,
										CountryManager.getInstance().getCountryById(countryId));
								CountryManager.getInstance().getCountryById(countryId)
										.sendPackAll(new SM_SpecialBoss_Die());
							}
						});
						spawnManager.bringIntoWorld(boss, 1);

						CountryManager.getInstance().getCountryById(countryId)
								.sendPackAll(new SM_SpecialBoss_Refresh());
					}

				}

				@Override
				public String getName() {
					return "绝世BOSS刷新";
				}
			}, cronTime);
		}

	}

	private void preNotice() {
		for (String cron : config.SPECIAL_BOSS_REFRESH_NOTICE_TIME.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (!config.getPublicTestConditions().verify(null)
							&& !ServerState.getInstance().isCelebrateActivityOpen(ActivityEnum.BOSS)) {
						return;
					}

					for (CountryId id : CountryId.values()) {
						// 已经存在的
						if (specialBossIsAlive(id)) {
							continue;
						}
						// notice
						I18nUtils content = I18nUtils.valueOf(config.SPECIAL_BOSS_NOTICE_I18N.getValue());
						ChatManager.getInstance().sendSystem(config.SPECIAL_BOSS_NOTICE_CHANNEL.getValue(), content,
								null, CountryManager.getInstance().getCountryById(id));
					}

				}

				@Override
				public String getName() {
					return "提前5分钟刷新公告";
				}
			}, cron);
		}
	}

	public boolean specialBossIsAlive(CountryId countryId) {
		return countrySpeicalBoss.containsKey(countryId);
	}

	public static OpenActiveManager getInstance() {
		return INSTANCE;
	}

	/**
	 * 经验大放送
	 * 
	 * @param player
	 * @param resourceId
	 */
	public void drawExpActive(Player player, String resourceId, int sign) {
		ExpActive expActive = player.getOpenActive().getExpActive();

		if (expActive.getRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EXPACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveExpResource resource = config.expStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		CoreActions actions = resource.getCoreActions();
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_EXP_ACT, resourceId));

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_EXP, resourceId));
		expActive.getRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	/**
	 * 橙色灵魂礼包
	 * 
	 * @param player
	 * @param resourceId
	 */
	public void drawEquipActive(Player player, String resourceId, int sign) {
		EquipActive equipActive = player.getOpenActive().getEquipActive();

		if (equipActive.getRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EQUIPACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveEquipResource resource = config.equipStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		CoreActions actions = resource.getCoreActions();
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_ORANGE_ACT, resourceId));

		rewardManager.grantReward(player,
				ChooserManager.getInstance().chooseValueByRequire(player, resource.getRewardChooserId()),
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_ORANGE, resourceId));
		equipActive.getRewarded().add(resourceId);
		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	/**
	 * 领取坐骑进阶奖励
	 * 
	 * @param player
	 * @param resourceId
	 * @param sign
	 */
	public void drawHorseUpgradeActiveReward(Player player, String resourceId, int sign) {
		HorseUpgradeActive horseActive = player.getOpenActive().getHorseUpgradeActive();

		if (horseActive.getActivityRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EXPACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveHorseUpgradeResource resource = config.horseUpgradeStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_HORSE, resourceId));
		horseActive.getActivityRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	public void drawSoulUpgradeActiveReward(Player player, String resourceId, int sign) {
		SoulActive soulActive = player.getOpenActive().getSoulActive();

		if (soulActive.getActivityRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EXPACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveSoulUpgradeResource resource = config.soulUpgradeStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_SOUL, resourceId));
		soulActive.getActivityRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	public void drawOldSoulUpgradeActiveReward(Player player, String resourceId) {
		OldSoulActive soulActive = player.getOpenActive().getOldSoulActive();

		if (soulActive.getActivityRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EXPACTIVE_REWARD_HAS_DRAW);
		}

		OldOpenActiveSoulUpgradeResource resource = config.oldSoulUpgradeStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_SOUL, resourceId));
		soulActive.getActivityRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_Old_SoulUpgrade_Draw_Reward.valueOf(resourceId));
	}

	/**
	 * 领取神兵进阶奖励
	 * 
	 * @param player
	 * @param resourceId
	 * @param sign
	 */
	public void drawArtifactUpgradeActiveReward(Player player, String resourceId, int sign) {
		ArtifactActive artifactActive = player.getOpenActive().getArtifactActive();

		if (artifactActive.getActivityRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EXPACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveArtifactResource resource = config.artifactActiveStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_ARTIFACT, resourceId));
		artifactActive.getActivityRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	public void drawCompeteReward(Player player, String id) {
		OpenActiveCompeteResource resource = config.competeActiveStorage.get(id, true);

		if (player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded().contains(id)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.COMPETE_REWARD_ALREADY_RECIEVED);
		}

		if (!resource.getRecieveConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		Reward reward = rewardManager.creatReward(player, rewardIds, null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_COMPETE, id));
		player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded().add(id);
		player.getOpenActive().getCanRecieved().remove(id);

		PacketSendUtility.sendPacket(player, SM_Draw_CompeteReward.valueOf(resource.getRankType(), id));
	}

	public void drawOldCompeteReward(Player player, String id) {
		OldOpenActiveCompeteResource resource = config.oldCompeteActiveStorage.get(id, true);

		if (player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded().contains(id)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.COMPETE_REWARD_ALREADY_RECIEVED);
		}

		if (!resource.getRecieveConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		Reward reward = rewardManager.creatReward(player, rewardIds, null);
		if (!rewardManager.playerPackCanholdAll(player, reward)) {
			throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_COMPETE, id));
		player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded().add(id);
		player.getOpenActive().getOldCanRecieved().remove(id);

		PacketSendUtility.sendPacket(player, SM_Draw_OldCompeteReward.valueOf(resource.getRankType(), id));
	}

	/**
	 * 每日充值
	 * 
	 * @param player
	 * @param code
	 */
	public void drawEveryDayRecharge(Player player, String code) {
		player.getOpenActive().getEveryDayRecharge().drawReward(code);
	}

	public void compensateRewardByEmail(Player player) {
		for (OpenActiveCompeteResource resource : config.competeActiveStorage.getAll()) {
			if (resource.getMailCompensateConditions().verify(player, false)) {
				if (!player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded()
						.contains(resource.getId())) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							resource.getChooserGroupId());
					Reward reward = rewardManager.creatReward(player, rewardIds, null);
					Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailI18nTitle()),
							I18nUtils.valueOf(resource.getMailI18nContent()), null, reward);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded()
							.add(resource.getId());
					player.getOpenActive().getCanRecieved().remove(resource.getId());
				}
			}
		}

		for (OldOpenActiveCompeteResource resource : config.oldCompeteActiveStorage.getAll()) {
			if (resource.getMailCompensateConditions().verify(player, false)) {
				if (!player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded()
						.contains(resource.getId())) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							resource.getChooserGroupId());
					Reward reward = rewardManager.creatReward(player, rewardIds, null);
					Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailI18nTitle()),
							I18nUtils.valueOf(resource.getMailI18nContent()), null, reward);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					player.getOpenActive().getCompeteRankActivity(resource.getRankType()).getRewarded()
							.add(resource.getId());
					player.getOpenActive().getOldCanRecieved().remove(resource.getId());
				}
			}
		}
		
		for (OpenActiveArtifactResource resource : config.artifactActiveStorage.getAll()) {
			if (resource.getMailTimeCondition().verify(player, false) && 
					player.getOpenActive().getArtifactActive().getMaxGrade() >= resource.getMinGrade() &&
						!player.getOpenActive().getArtifactActive().getActivityRewarded().contains(resource.getId())) {
				Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
				Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailTitle()),
						I18nUtils.valueOf(resource.getMailContent()), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				player.getOpenActive().getArtifactActive().getActivityRewarded().add(resource.getId());
			}
		}
		
		for (OpenActiveHorseUpgradeResource resource : config.horseUpgradeStorage.getAll()) {
			if (resource.getMailTimeCondition().verify(player, false) && 
					player.getOpenActive().getHorseUpgradeActive().getMaxGrade() >= resource.getMinGrade() &&
						!player.getOpenActive().getHorseUpgradeActive().getActivityRewarded().contains(resource.getId())) {
				Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
				Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailTitle()),
						I18nUtils.valueOf(resource.getMailContent()), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				player.getOpenActive().getHorseUpgradeActive().getActivityRewarded().add(resource.getId());
			}
		}
		
		for (OpenActiveSoulUpgradeResource resource : config.soulUpgradeStorage.getAll()) {
			if (resource.getMailTimeCondition().verify(player, false) && 
					player.getOpenActive().getSoulActive().getMaxGrade() >= resource.getMinGrade() &&
						!player.getOpenActive().getSoulActive().getActivityRewarded().contains(resource.getId())) {
				Reward reward = rewardManager.creatReward(player, resource.getRewardId(), null);
				Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailTitle()),
						I18nUtils.valueOf(resource.getMailContent()), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				player.getOpenActive().getSoulActive().getActivityRewarded().add(resource.getId());
			}
		}
	}

	public int getSevenCompeteRewardCount(Player player) {
		for (CompeteRankValue rankActivity : CompeteRankValue.values()) {
			if (rankActivity == CompeteRankValue.OLD_SOUL_RANK) {
				continue;
			}
			player.getOpenActive().getCanRecieved().addAll(rankActivity.getCanRewardCount(player));
		}
		return player.getOpenActive().getCanRecieved().size();
	}

	public int getOldSevenCompeteRewardCount(Player player) {
		for (CompeteRankValue rankActivity : CompeteRankValue.values()) {
			if (rankActivity != CompeteRankValue.OLD_SOUL_RANK) {
				continue;
			}
			player.getOpenActive().getOldCanRecieved().addAll(rankActivity.getOldCanRewardCount(player));
		}
		return player.getOpenActive().getOldCanRecieved().size();
	}

	public Map<Integer, CompeteVO> getCompeteRewardStatus(Player player) {
		Map<Integer, CompeteVO> ret = new HashMap<Integer, CompeteVO>();
		for (CompeteRankValue rankActivity : CompeteRankValue.values()) {
			if (rankActivity == CompeteRankValue.OLD_SOUL_RANK) {
				continue;
			}
			ret.put(rankActivity.getRankTypeValue(), CompeteVO.valueOf(rankActivity, player));
		}
		return ret;
	}

	public Map<Integer, OldCompeteVO> getOldCompeteRewardStatus(Player player) {
		Map<Integer, OldCompeteVO> ret = new HashMap<Integer, OldCompeteVO>();
		ret.put(CompeteRankValue.OLD_SOUL_RANK.getRankTypeValue(),
				OldCompeteVO.valueOf(CompeteRankValue.OLD_SOUL_RANK, player));
		return ret;
	}

	public void rewardStarItem(Player player) {
		if (player.getOpenActive().getStarItemActive().isRewarded()) {
			return;
		}
		I18nUtils titel18n = I18nUtils.valueOf(config.STARITEM_REWARD_MAIL_TITLE_I18N.getValue());
		I18nUtils contextl18n = I18nUtils.valueOf(config.STARITEM_REWARD_MAIL_CONTENT_I18N.getValue());
		Reward reward = RewardManager.getInstance().creatReward(player, config.STARITEM_REWARDID.getValue(), null);
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
		mailManager.sendMail(mail, player.getObjectId());
		player.getOpenActive().getStarItemActive().setRewarded(true);
	}

	public void drawEnhanceEquipActive(Player player, String resourceId, int sign) {
		EnhancePowerActive enhancePowerActive = player.getOpenActive().getEnhancePowerActive();

		if (enhancePowerActive.getRewarded().contains(resourceId)) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.ENHANCE_POWER_ACTIVE_REWARD_HAS_DRAW);
		}

		OpenActiveEnhanceResource resource = config.enhancePowerActiveStorage.get(resourceId, true);
		if (!resource.getCoreConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		CoreActions actions = resource.getCoreActions();
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_EXP_ACT, resourceId));

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserId());
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.ENHANCE_POWER_ACTIVE, resourceId));
		enhancePowerActive.getRewarded().add(resourceId);

		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	public void receiveCollectItemActiveReward(Player player, int sign, String resourceId, int count) {
		Reward rewards = Reward.valueOf();
		OpenActiveCollectItemResource resource = OpenActiveConfig.getInstance().collectItemStorage
				.get(resourceId, true);
		if (!resource.getCondtions(count).verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!resource.getActions(count).verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		resource.getActions(count).act(player,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_CONSUME_ACT));
		String[] rewardIds = new String[count];
		Arrays.fill(rewardIds, resource.getRewardId());
		rewards.addReward(RewardManager.getInstance().creatReward(player, Arrays.asList(rewardIds), null));
		RewardManager.getInstance().grantReward(player, rewards,
				ModuleInfo.valueOf(ModuleType.OPENACTIVE, SubModuleType.OPENACTIVE_COLLECTITEM_REWARD));
		PacketSendUtility.sendPacket(player, SM_OpenActive_Reward.valueOf(sign, resourceId));
	}

	public void checkAndSendRewardStatus(Player player, int rankTypeValue) {
		for (OpenActiveCompeteResource resource : OpenActiveConfig.getInstance().getInstanceRecieveResource(
				rankTypeValue)) {
			if (resource.getRecieveConditions().verify(player)
					&& !player.getOpenActive().getCompeteRankActivity(rankTypeValue).getRewarded()
							.contains(resource.getId())) {
				player.getOpenActive().getCanRecieved().add(resource.getId());
				PacketSendUtility.sendPacket(
						player,
						SM_SevenCompete_Push.valueOf(player, rankTypeValue,
								CompeteVO.valueOf(CompeteRankValue.valueOf(rankTypeValue), player)));
			} else if (rankTypeValue == CompeteRankValue.ENHANCE_RANK.getRankTypeValue()) {
				player.getOpenActive().getCanRecieved().remove(resource.getId());
				PacketSendUtility.sendPacket(
						player,
						SM_SevenCompete_Push.valueOf(player, rankTypeValue,
								CompeteVO.valueOf(CompeteRankValue.valueOf(rankTypeValue), player)));
			}
		}
	}
	
	public void handleCountryHero(PlayerDieEvent event, Player killer) {
		OpenActiveCompeteResource resource = config
				.getSpecifiedRankTypeResource(RankType.ACTIVITY_COUNTRY_HERO.getValue());
		if (resource.getLogDataConditions().verify(killer)) {
			killer.getOpenActive().getCountryHeroActive().addKillNums(1);
		}

		if (resource.getEnterRankConditions().verify(killer, false)) {
			PlayerDieEvent e = PlayerDieEvent.valueOf(event.getPlayerId(), killer.getObjectId(), true);
			e.setExtra(killer.getOpenActive().getCountryHeroActive().getKillNums());
			WorldRankManager.getInstance().submitRankRow(killer, RankType.ACTIVITY_COUNTRY_HERO.getCountryRank(killer.getCountryValue()), e);
		}

		checkAndSendRewardStatus(killer, CompeteRankValue.ACTIVITY_COUNTRY_HERO.getRankTypeValue());
	}

	public void checkOldAndSendRewardStatus(Player player, int rankTypeValue) {
		for (OldOpenActiveCompeteResource resource : OpenActiveConfig.getInstance().getOldInstanceRecieveResource(
				rankTypeValue)) {
			if (resource.getRecieveConditions().verify(player)
					&& !player.getOpenActive().getCompeteRankActivity(rankTypeValue).getRewarded()
							.contains(resource.getId())) {
				player.getOpenActive().getOldCanRecieved().add(resource.getId());
				PacketSendUtility.sendPacket(
						player,
						SM_OldSevenCompete_Push.valueOf(player, rankTypeValue,
								OldCompeteVO.valueOf(CompeteRankValue.valueOf(rankTypeValue), player)));
			}
		}
	}

	public void receiveGroupPurchaseReward(Player player, String id) {
		if (ServerState.getInstance().getPlayerGroupPurchases() == null) {
			return;
		}
		GroupPurchase groupPurchase = ServerState.getInstance().getPlayerGroupPurchases().get(player.getObjectId());
		if (groupPurchase == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (groupPurchase.getRewarded().contains(id)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!config.getGroupPurchaseTimeConds().verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}

		GroupPurchaseResource resource = config.groupPurchaseStorage.get(id, true);
		if (!resource.getDrawCondition().verify(player.getObjectId(), true)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserId());
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.PUBLIC_BETA, SubModuleType.PUBLIC_BETA_GROUP_PURCHASE_REWARD, id));
		groupPurchase.addRewarded(id);
		PacketSendUtility.sendPacket(player, SM_GroupPurchase_Reward.valueOf(id));
	}

	public void receiveGroupPurchaseTwoReward(Player player, String id) {
		if (null == ServerState.getInstance().getPlayerGroupPurchases2()) {
			return;
		}
		GroupPurchaseTwo groupPurchaseTwo = ServerState.getInstance().getPlayerGroupPurchases2()
				.get(player.getObjectId());
		if (groupPurchaseTwo == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (groupPurchaseTwo.getRewarded().contains(id)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!config.getGroupPurchaseTwoTimeConds().verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}
		GroupPurchaseTwoResource resource = config.groupPurchaseTwoStorage.get(id, true);
		if (!resource.getDrawCondition().verify(player.getObjectId(), true)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserId());
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.PUBLIC_BETA, SubModuleType.GROUPPURCHASE_TWO_REWARD, id));
		groupPurchaseTwo.addRewarded(id);
		PacketSendUtility.sendPacket(player, SM_GroupPurchase_Two_Reward.valueOf(id));
	}

	public void receiveGroupPurchaseThreeReward(Player player, String id) {
		GroupPurchaseThree groupPurchaseThree = ServerState.getInstance().getPlayerGroupPurchases3()
				.get(player.getObjectId());
		if (groupPurchaseThree == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (groupPurchaseThree.getRewarded().contains(id)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!config.getGroupPurchaseThreeTimeConds().verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}
		GroupPurchaseThreeResource resource = config.groupPurchaseThreeStorage.get(id, true);
		if (!resource.getDrawCondition().verify(player.getObjectId(), true)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserId());
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.PUBLIC_BETA, SubModuleType.GROUPPURCHASE_THREE_REWARD, id));
		groupPurchaseThree.addRewarded(id);
		PacketSendUtility.sendPacket(player, SM_GroupPurchase_Three_Reward.valueOf(id));
	}

	public SM_GroupPurchaseInfo getGroupPurchaseInfo(Player player) {
		SM_GroupPurchaseInfo result = SM_GroupPurchaseInfo.valueOf(player);
		return result;
	}

	public SM_GroupPurchaseTwoInfo getGroupPurchaseTwoInfo(Player player) {
		SM_GroupPurchaseTwoInfo result = SM_GroupPurchaseTwoInfo.valueOf(player);
		return result;
	}

	public SM_GroupPurchaseThreeInfo getGroupPurchaseThreeInfo(Player player) {
		SM_GroupPurchaseThreeInfo result = SM_GroupPurchaseThreeInfo.valueOf(player);
		return result;
	}

	public void rewardGroupPurchaseMail() {
		if (OpenActiveConfig.getInstance().getGroupPurchaseMailTimeConds().verify(null)) {
			if (ServerState.getInstance().getPlayerGroupPurchases() == null) {
				return;
			}
			NonBlockingHashMap<String, NonBlockingHashSet<Long>> attendGroupPurchase = ServerState.getInstance()
					.getGroupPurchasePlayers();
			if (attendGroupPurchase == null) {
				return;
			}
			for (Entry<String, NonBlockingHashSet<Long>> entry : attendGroupPurchase.entrySet()) {
				GroupPurchaseResource resource = OpenActiveConfig.getInstance().groupPurchaseStorage.get(
						entry.getKey(), true);
				for (Long playerId : entry.getValue()) {
					if (ServerState.getInstance().getPlayerGroupPurchases() == null) {
						return;
					}
					GroupPurchase playerGroupPurchase = ServerState.getInstance().getPlayerGroupPurchases()
							.get(playerId);
					boolean rewarded = playerGroupPurchase.isRewarded(resource.getId());
					boolean verified = resource.getDrawCondition().verify(playerId);
					if (!rewarded && verified) {
						Player player = PlayerManager.getInstance().getPlayer(playerId);
						List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
								resource.getRewardChooserId());
						Reward reward = rewardManager.creatReward(player, rewardIds, null);
						I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitle());
						I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailContent());
						Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
						MailManager.getInstance().sendMail(mail, player.getObjectId());
					}
				}
			}
			ServerState.getInstance().getPlayerGroupPurchases().clear();
			ServerState.getInstance().getGroupPurchasePlayers().clear();
		}
	}

	public void rewardGroupPurchaseTwoMail() {
		if (OpenActiveConfig.getInstance().getGroupPurchaseTwoMailTimeConds().verify(null)) {
			NonBlockingHashMap<String, NonBlockingHashSet<Long>> attendGroupPurchase = ServerState.getInstance()
					.getGroupPurchasePlayers2();
			if (null == attendGroupPurchase) {
				return;
			}
			for (Entry<String, NonBlockingHashSet<Long>> entry : attendGroupPurchase.entrySet()) {
				GroupPurchaseTwoResource resource = OpenActiveConfig.getInstance().groupPurchaseTwoStorage.get(
						entry.getKey(), true);
				for (Long playerId : entry.getValue()) {
					GroupPurchaseTwo playerGroupPurchaseTwo = ServerState.getInstance().getPlayerGroupPurchases2()
							.get(playerId);
					boolean rewarded = playerGroupPurchaseTwo.isRewarded(resource.getId());
					boolean verified = resource.getDrawCondition().verify(playerId);
					if (!rewarded && verified) {
						Player player = PlayerManager.getInstance().getPlayer(playerId);
						List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
								resource.getRewardChooserId());
						Reward reward = rewardManager.creatReward(player, rewardIds, null);
						I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitle());
						I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailContent());
						Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
						MailManager.getInstance().sendMail(mail, player.getObjectId());
					}
				}
			}
			ServerState.getInstance().getPlayerGroupPurchases2().clear();
			ServerState.getInstance().getGroupPurchasePlayers2().clear();
		}
	}

	public void rewardGroupPurchaseThreeMail() {
		if (OpenActiveConfig.getInstance().getGroupPurchaseThreeMailTimeConds().verify(null)) {
			NonBlockingHashMap<String, NonBlockingHashSet<Long>> attendGroupPurchase = ServerState.getInstance()
					.getGroupPurchasePlayers3();
			if (attendGroupPurchase == null) {
				return;
			}
			for (Entry<String, NonBlockingHashSet<Long>> entry : attendGroupPurchase.entrySet()) {
				GroupPurchaseThreeResource resource = OpenActiveConfig.getInstance().groupPurchaseThreeStorage.get(
						entry.getKey(), true);
				for (Long playerId : entry.getValue()) {
					GroupPurchaseThree playerGroupPurchaseThree = ServerState.getInstance().getPlayerGroupPurchases3()
							.get(playerId);
					boolean rewarded = playerGroupPurchaseThree.isRewarded(resource.getId());
					boolean verified = resource.getDrawCondition().verify(playerId);
					if (!rewarded && verified) {
						Player player = PlayerManager.getInstance().getPlayer(playerId);
						List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
								resource.getRewardChooserId());
						Reward reward = rewardManager.creatReward(player, rewardIds, null);
						I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitle());
						I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailContent());
						Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
						MailManager.getInstance().sendMail(mail, player.getObjectId());
					}
				}
			}
			ServerState.getInstance().getPlayerGroupPurchases3().clear();
			ServerState.getInstance().getGroupPurchasePlayers3().clear();
		}
	}

	public void rewardCelebrateRecharge() {
		if (!ServerState.getInstance().isCelebrateActivityOpen(ActivityEnum.RECHARGE)
				&& !ServerState.getInstance().getCelebrateRecharge().isEmpty()) {
			for (Entry<Long, RechargeCelebrate> entry : ServerState.getInstance().getCelebrateRecharge().entrySet()) {
				if (entry.getValue().getRewardedId() == null
					&& entry.getValue().getRechargeAmount() >= OpenActiveConfig.getInstance().RECHARGE_MINIMAL_GOLD_REQUIRED.getValue()) {
					Player player = PlayerManager.getInstance().getPlayer(entry.getKey());
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							config.RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue());
					Reward reward = rewardManager.creatReward(player, rewardIds, null);
					I18nUtils titel18n = I18nUtils.valueOf(config.RECHARGE_CELEBRATE_COMPENSATE_MAIL_TITLE.getValue());
					I18nUtils contextl18n = I18nUtils.valueOf(config.RECHARGE_CELEBRATE_COMPENSATE_MAIL_CONTENT
							.getValue());
					Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
					MailManager.getInstance().sendMail(mail, player.getObjectId());
					entry.getValue().setRewardedId(config.RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue());
				}
			}
			ServerState.getInstance().getCelebrateRecharge().clear();
		}
	}

	/**
	 * 公测献礼活动购买奖励
	 * 
	 * @param player
	 * @param giftId
	 */
	public SM_Public_Test_Gift_Reward receiveNextPublicTestGiftReward(Player player, String giftId) {
		PublicTestGiftResource giftResource = config.giftResource.get(giftId, true);
		// 判断是否是公测当天购买
		if (!config.getDoubleElementCeremonyTime().verify(player, false)) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_TIME);
		}
		// 判断是否已经购买
		GiftActive giftActive = player.getOpenActive().getGiftActive();
		if (giftActive.hasBuyBefore(giftResource)) {// 如果已经购买了
			throw new ManagedException(ManagedErrorCode.PUBLIC_TEST_GIFT_HAS_BUY);
		}
		// 判断是否购买了上一级别
		if (!giftActive.hasBuyLowLevel(giftResource)) {
			throw new ManagedException(ManagedErrorCode.PUBLIC_TEST_GIFT_HAS_NOT_BUY_CONDITION);
		}
		// 判断是否满足购买条件
		if (!giftResource.getCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		// 判断消耗是否充足
		giftResource.getCoreActions().verify(player, true);
		// 开始购买
		giftResource.getCoreActions().act(player,
				ModuleInfo.valueOf(ModuleType.PUBLIC_BETA, SubModuleType.PUBLIC_TEST_GIFT_ACT, giftId));
		player.getOpenActive().getGiftActive().getGifted().add(giftId);
		// 开始发奖
		RewardManager.getInstance().grantReward(player, Arrays.asList(giftResource.getRewardIds()),
				ModuleInfo.valueOf(ModuleType.PUBLIC_BETA, SubModuleType.PUBLIC_TEST_REWARD_GRANT, giftId));
		// 可以购买的下一档
		SM_Public_Test_Gift_Reward res = SM_Public_Test_Gift_Reward.valueof(giftResource.getGroupId(),
				giftResource.getHighLevelId());
		return res;
	}

	public void soulUpgradeEventHandler(Player player, SoulUpgradeEvent event, boolean sendPack) {
		if (OpenActiveConfig.getInstance().getOldSoulActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getSoulActive().setMaxGrade(event.getGrade());
		}
		OldOpenActiveCompeteResource oldResource = OpenActiveConfig.getInstance().getOldSpecifiedRankTypeResource(
				RankType.OLD_ACTIVITY_SOUL.getValue());
		if (oldResource.getLogDataConditions().verify(player)) {
			player.getOpenActive().getOldSoulActive().setRankMaxGrade(event.getGrade());
		}

		if (oldResource.getEnterRankConditions().verify(player, false)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.OLD_ACTIVITY_SOUL, event);
		}

		if (OpenActiveConfig.getInstance().getSoulActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getSoulActive().setMaxGrade(event.getGrade());
		}
		OpenActiveCompeteResource resource = OpenActiveConfig.getInstance().getSpecifiedRankTypeResource(
				RankType.ACTIVITY_SOUL.getValue());
		if (resource.getLogDataConditions().verify(player)) {
			player.getOpenActive().getSoulActive().setRankMaxGrade(event.getGrade());
		}

		if (resource.getEnterRankConditions().verify(player, false)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_SOUL, event);
		}

		if (sendPack) {
			checkAndSendRewardStatus(player, CompeteRankValue.SOUL_RANK.getRankTypeValue());
			checkOldAndSendRewardStatus(player, CompeteRankValue.OLD_SOUL_RANK.getRankTypeValue());
		}
	}

	public SM_Draw_CelebrateReward drawCelebrateReward(Player player) {
		RechargeCelebrate rc = ServerState.getInstance().getCelebrateRecharge().get(player.getObjectId());
		if (rc == null || rc.getRechargeAmount() < config.RECHARGE_MINIMAL_GOLD_REQUIRED.getValue()) {
			throw new ManagedException(ManagedErrorCode.RECHARGE_NOT_ENOUGH);
		}
		if (rc.getRewardedId() != null
				&& rc.getRewardedId().equals(config.RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue())) {
			throw new ManagedException(ManagedErrorCode.RECHARGE_ALREADY_RECIEVED_REWARD);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				config.RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue());

		Reward reward = rewardManager.creatReward(player, rewardIds);

		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.CELEBRATE, SubModuleType.CELEBRATE_RECHARGE_REWARD));
		rc.setRewardedId(config.RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP.getValue());
		return SM_Draw_CelebrateReward.valueOf(reward);
	}

	public void rewardCelebrateFirework(Player player, int count) {
		ActivityInfo fireworkActivity = ServerState.getInstance().getCelebrateActivityInfos()
				.get(ActivityEnum.FIREWORK.getValue());
		if (fireworkActivity == null || !fireworkActivity.isOpenning()) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_PERIOD);
		}
		if (!OpenActiveConfig.getInstance().getFireworkAttendConds().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CoreActions coreActions = OpenActiveConfig.getInstance().getFireworkActions(count);
		if (!coreActions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		coreActions.act(player, ModuleInfo.valueOf(ModuleType.CELEBRATE, SubModuleType.CELEBRATE_FIREWORK_ACT));

		String[] rewardIds = OpenActiveConfig.getInstance().CELEBRATE_FIREWORK_REWARDIDS.getValue();

		Reward reward = rewardManager.creatReward(player, Arrays.asList(rewardIds));
		reward.mutipleRewards(count);

		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.CELEBRATE, SubModuleType.CELEBRATE_FIREWORK_REWARD));
		PacketSendUtility.sendPacket(player, new SM_Celebrate_Reward());
	}
}
