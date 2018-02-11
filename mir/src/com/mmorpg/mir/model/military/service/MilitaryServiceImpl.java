package com.mmorpg.mir.model.military.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.military.manager.MilitaryManager;
import com.mmorpg.mir.model.military.model.Military;
import com.mmorpg.mir.model.military.packet.CM_Break_MilitaryStratege;
import com.mmorpg.mir.model.military.packet.CM_strategy_upgrade;
import com.mmorpg.mir.model.military.packet.SM_Break_MilitaryStratege;
import com.mmorpg.mir.model.military.packet.SM_Military_Star_Change;
import com.mmorpg.mir.model.military.packet.SM_strategy_update;
import com.mmorpg.mir.model.military.resource.MilitaryLevelResource;
import com.mmorpg.mir.model.military.resource.MilitaryStarResource;
import com.mmorpg.mir.model.military.resource.MilitaryStrategyResource;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.RandomUtils;

@Component
public class MilitaryServiceImpl implements MilitaryService {

	@Autowired
	private MilitaryManager manager;

	@Autowired
	private RewardManager reward;

	@Static("MILITARY:LOCAL_HONOR_REWARD")
	private ConfigValue<Integer> LOCAL_HONOR_REWARD;

	@Static("MILITARY:HOSTIL_HONOR_REWARD")
	private ConfigValue<Integer> HOSTIL_HONOR_REWARD;

	@Static("MILITARY:NEATUAL_HONOR_REWARD")
	private ConfigValue<Integer> NEATUAL_HONOR_REWARD;

	@Static("MILITARY:DUPLICATE_KILL")
	private ConfigValue<Integer> DUPLICATE_KILL;

	@Static("MILITARY:RANK_GAP_MIN")
	private ConfigValue<Integer> RANK_GAP_MIN;

	@Static("MILITARY:RANK_GAP_MID")
	private ConfigValue<Integer> RANK_GAP_MID;

	@Static("MILITARY:RANK_GAP_MAX")
	private ConfigValue<Integer> RANK_GAP_MAX;

	@Static("MILITARY:RANK_GAP_MIN_RATE")
	private ConfigValue<Integer> RANK_GAP_MIN_RATE;

	@Static("MILITARY:RANK_GAP_MID_RATE")
	private ConfigValue<Integer> RANK_GAP_MID_RATE;

	@Static("MILITARY:RANK_GAP_MAX_RATE")
	private ConfigValue<Integer> RANK_GAP_MAX_RATE;

	@Static("MILITARY:TODAT_MAX_HONOR")
	private ConfigValue<Integer> TODAY_MAX_HONOR;

	@Static("MILITARY:HONOR_INCEMENT")
	private Formula HONOR_INCEMENT;

	private static MilitaryService INSTANCE;

	@PostConstruct
	public void init() {
		INSTANCE = this;
	}

	public static MilitaryService getInstance() {
		return INSTANCE;
	}

	/** 击杀获得荣誉 */
	public void killAddHonor(Player killer, Player killed) {

		CoreConditions conditions = manager.getResource(killer.getMilitary().getRank()).getHonorConditions();
		Map<String, Player> context = New.hashMap();
		context.put(TriggerContextKey.PLAYER, killer);
		context.put(TriggerContextKey.OTHER_PLAYER, killed);

		if (!conditions.verify(context)) {
			return;
		}

		// 获取当前地域初始收益
		MapCountry location = World.getInstance().getWorldMap(killer.getMapId()).getCountry();
		int original = 0;
		if (location == MapCountry.NEUTRAL) {
			original = NEATUAL_HONOR_REWARD.getValue();
		} else if (location.getValue() == killer.getCountryValue()) {
			original = LOCAL_HONOR_REWARD.getValue();
		} else {
			original = HOSTIL_HONOR_REWARD.getValue();
		}

		// 计算阶差加成
		int rankGap = killed.getMilitary().getRank() - killer.getMilitary().getRank();
		double rate = 0.0;
		if (rankGap >= RANK_GAP_MIN.getValue() && rankGap < RANK_GAP_MID.getValue()) {
			rate += (RANK_GAP_MIN_RATE.getValue() * 1.0 / 10000);
		} else if (rankGap >= RANK_GAP_MIN.getValue() && rankGap < RANK_GAP_MAX.getValue()) {
			rate += (RANK_GAP_MID_RATE.getValue() * 1.0 / 10000);
		} else if (rankGap >= RANK_GAP_MAX.getValue()) {
			rate += (RANK_GAP_MAX_RATE.getValue() * 1.0 / 10000);
		}

		// 计算X分钟内，杀同一个人的荣誉收益递减
		Long lastKillTime = killer.getMilitary().getKillLastTime().get(killed.getObjectId());
		Integer killCount = killer.getMilitary().getKillCount().get(killed.getObjectId());
		long now = System.currentTimeMillis();
		Integer update = 1;

		if (lastKillTime != null && killCount != null && (now - lastKillTime) < (DUPLICATE_KILL.getValue() * 1000L)) {
			update += killCount;
		}

		killer.getMilitary().getKillCount().put(killed.getObjectId(), update);
		killer.getMilitary().getKillLastTime().put(killed.getObjectId(), now);

		int todayHonor = killer.getMilitary().refreshAndGetTodayHonor();
		if (TODAY_MAX_HONOR.getValue() <= todayHonor) {
			// 今天已达到最大杀人荣誉
			throw new ManagedException(ManagedErrorCode.TODAY_HONOR_IS_MAX);
		}

		int calHonor = (Integer) FormulaParmsUtil.valueOf(HONOR_INCEMENT).addParm("count", update)
				.addParm("rate", rate).addParm("original", original).getValue();// (int)
																				// Math.ceil(1.0
		// / (update - 1) *
		// original * rate)

		if (calHonor <= 0) {
			return;
		}

		int realAddHonor = killer.getMilitary().addTodayHonor(calHonor, TODAY_MAX_HONOR.getValue());

		reward.grantReward(killer, Reward.valueOf().addCurrency(CurrencyType.HONOR, (Integer) realAddHonor),
				ModuleInfo.valueOf(ModuleType.MILITARY, SubModuleType.MILITARY_KILL));
	}

	/** 军衔升级 */
	public void upgradeMilitaryRank(Player player) {
		player.getMilitary().upgradeRank();
		player.getGameStats().replaceModifiers(Military.MILITARY_STAT,
				manager.getResource(player.getMilitary().getRank()).getMilitaryStats(), true);

		final int militaryRank = player.getMilitary().getRank();
		MilitaryLevelResource res = MilitaryManager.getInstance().getResource(militaryRank);
		if (res.getNoticeI18nId() != null) {
			I18nUtils utils = I18nUtils.valueOf(res.getNoticeI18nId());
			utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm(I18NparamKey.MILITARYNAME, I18nPack.valueOf(res.getNameI18n()));
			ChatManager.getInstance().sendSystem(res.getChannelId(), utils, null, player.getCountry());
		}

	}

	public void upgradeMilitaryStar(Player player) {
		Military playerMilitary = player.getMilitary();
		MilitaryStarResource resource = manager.getMilitaryStarResource(playerMilitary.getStarId(), false);
		if (resource == null) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
			return;
		}
		if (!resource.getUpConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MILITARY_MAX_STAR);
			return;
		}

		resource = manager.getMilitaryStarResource(resource.getNextId(), true);
		playerMilitary.setStarId(resource.getId());

		Skill skill = SkillEngine.getInstance().getSkill(null, resource.getStarSkillId(), player.getObjectId(), 0, 0,
				player, null);
		skill.noEffectorUseSkill();
		PacketSendUtility.sendPacket(player, SM_Military_Star_Change.valueOf(0, playerMilitary.getStarId()));

		if (player.getMilitary().getRank() == MilitaryManager.getInstance().INITIAL_STAR_RANK.getValue()) {
			player.getMilitary().setLastStarActionTime(System.currentTimeMillis());
		}
	}

	/** 军衔兵法升级 */
	public void upgradeMilitaryStrategy(Player player, CM_strategy_upgrade req) {
		String moduleId = manager.getMilitaryStragyModuleOpenId(req.getSection());
		if (moduleId == null) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, moduleId)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			return;
		}

		if (req.getCode() != 0) {
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(0, player.getMilitary().getStrategy(),
					true, player.getMilitary().getBreaks()));
			return;
		}

		int resourceId = manager.getInitalId(req.getSection());
		resourceId += player.getMilitary().getStrategyLevelBySection(req.getSection());

		MilitaryStrategyResource resource = manager.getStrategyResource(resourceId);
		if (resource.getNextId() == 0) {
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL, player.getMilitary().getStrategy(), false, player
							.getMilitary().getBreaks()));
			return;
		}
		resource = manager.getStrategyResource(resource.getNextId());
		if (!resource.getConditions().verify(player, false)) {
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL, player.getMilitary().getStrategy(), false, player
							.getMilitary().getBreaks()));
			return;
		}
		// 必须突破
		if (resource.isNeedBreak() && !player.getMilitary().getBreaks().contains(resource.getId())) {
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL, player.getMilitary().getStrategy(), false, player
							.getMilitary().getBreaks()));
			return;
		}

		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, resource.getActions());
		if (!actions.verify(player, false)) {
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.UPGRADE_STRATEGY_CONDITION_NOT_ENOUGH, player.getMilitary().getStrategy(), false,
					player.getMilitary().getBreaks()));
			return;
		}

		actions.act(player, ModuleInfo.valueOf(ModuleType.MILITARY, SubModuleType.MILITARY_UPGRADE_STRATEGY));

		// 丁涛说的
		if (!player.getMilitary().getFailCount().containsKey(req.getSection())) {
			player.getMilitary().getFailCount().put(req.getSection(), 0);
		}

		if (resource.getMin() > player.getMilitary().getFailCount().get(req.getSection())) {
			// 必定失败
			player.getMilitary().upgrateFail(req.getSection());
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL_UNLUCKY, player.getMilitary().getStrategy(), false,
					player.getMilitary().getBreaks()));
		} else if (resource.getMax() <= player.getMilitary().getFailCount().get(req.getSection())
				|| RandomUtils.isHit(resource.getPercent() * 1.0 / 10000)) {
			// 成功
			int ret = player.getMilitary().upgradeStrategy(resourceId, req.getSection());
			if (ret != 0) {
				player.getGameStats().replaceModifiers(Military.MILITARY_STAT_STRATEGY[req.getSection() - 1],
						manager.getStrategyResource(ret).getRoleStat(player.getRole()), true);
			}
		} else {
			player.getMilitary().upgrateFail(req.getSection());
			PacketSendUtility.sendPacket(player, SM_strategy_update.valueOf(
					ManagedErrorCode.MILITARY_STRATEGY_UPGRADE_FAIL_UNLUCKY, player.getMilitary().getStrategy(), false,
					player.getMilitary().getBreaks()));
		}
	}

	@Override
	public void breakMilitaryStrategy(Player player, CM_Break_MilitaryStratege req) {
		String moduleId = manager.getMilitaryStragyModuleOpenId(req.getSection());
		if (moduleId == null) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, moduleId)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			return;
		}
		int resourceId = manager.getInitalId(req.getSection());
		resourceId += player.getMilitary().getStrategyLevelBySection(req.getSection());

		MilitaryStrategyResource resource = manager.getStrategyResource(resourceId);
		if (resource.getNextId() == 0) {
			PacketSendUtility.sendPacket(player,
					SM_Break_MilitaryStratege.valueOf(req.getSection(), ManagedErrorCode.MILITARY_BREAK_ERROR, 0));
			return;
		}
		resource = manager.getStrategyResource(resource.getNextId());
		// 突破
		if (!resource.isNeedBreak() || player.getMilitary().getBreaks().contains(resource.getId())) {
			PacketSendUtility.sendPacket(
					player,
					SM_Break_MilitaryStratege.valueOf(req.getSection(), ManagedErrorCode.MILITARY_BREAK_ERROR,
							resource.getId()));
			return;
		}
		// 消耗
		resource.getBreakAction().verify(player, true);
		boolean isBreak = false;
		if (req.isGold()) {
			resource.getBreakGoldAction().verify(player, true);
			resource.getBreakAction().act(player,
					ModuleInfo.valueOf(ModuleType.MILITARY, SubModuleType.MILITARY_BREAK_COIN));
			resource.getBreakGoldAction().act(player,
					ModuleInfo.valueOf(ModuleType.MILITARY, SubModuleType.MILITARY_BREAK_GOLD));
			player.getMilitary().getBreaks().add(resource.getId());
			PacketSendUtility.sendPacket(player,
					SM_Break_MilitaryStratege.valueOf(req.getSection(), 0, resource.getId()));
			isBreak = true;
		} else {
			resource.getBreakAction().act(player,
					ModuleInfo.valueOf(ModuleType.MILITARY, SubModuleType.MILITARY_BREAK_COIN));
			if (RandomUtils.isHit(resource.getBreakPercent() * 1.0 / 10000)) {
				player.getMilitary().getBreaks().add(resource.getId());
				PacketSendUtility.sendPacket(player,
						SM_Break_MilitaryStratege.valueOf(req.getSection(), 0, resource.getId()));
				isBreak = true;
			} else {
				PacketSendUtility.sendPacket(player, SM_Break_MilitaryStratege.valueOf(req.getSection(),
						ManagedErrorCode.MILITARY_BREAK_ERROR, resource.getId()));
			}
		}
		if (isBreak) {
			// 广播
			I18nUtils tvI18n = I18nUtils.valueOf(resource.getTvI18nId());
			tvI18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			tvI18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
			tvI18n.addParm(I18NparamKey.LEVEL,
					I18nPack.valueOf(player.getMilitary().getStrategyLevelBySection(req.getSection()) / 12));
			ChatManager.getInstance().sendSystem(resource.getTvChannel(), tvI18n, null);
		}
	}
}
