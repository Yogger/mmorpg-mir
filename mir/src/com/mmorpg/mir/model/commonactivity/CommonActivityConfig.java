package com.mmorpg.mir.model.commonactivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.commonactivity.model.RecollectType;
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
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopGoodResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRechargeActiveResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRecollectResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRedPacketResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonSPServerResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonTreasureActivityResource;
import com.mmorpg.mir.model.commonactivity.resource.LuckyDrawResource;
import com.mmorpg.mir.model.commonactivity.resource.WeekCriResource;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.BetweenTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CommonActivityConfig {
	private static CommonActivityConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static CommonActivityConfig getInstance() {
		return INSTANCE;
	}

	@Static
	public Storage<String, CommonDoubleExpResource> commonDBStorage;

	@Static
	public Storage<String, CommonSPServerResource> commonServerStorage;

	@Static
	public Storage<String, CommonMarcoShopGoodResource> commonShopGoodStorage;

	@Static
	public Storage<String, CommonMarcoShopResource> commonShopStorage;

	@Static
	public Storage<String, CommonLoginGiftResource> loginGiftStorage;

	@Static
	public Storage<String, CommonCheapGiftBagResource> cheapGiftBagStorage;

	@Static
	public Storage<String, CommonConsumeActiveResource> consumeStorages;

	@Static
	public Storage<String, CommonRechargeActiveResource> rechargeStorages;

	@Static
	public Storage<String, CommonFireCelebrateResource> fireStorages;

	@Static
	public Storage<String, CommonBossResource> bossStorage;

	@Static
	public Storage<String, CommonIdentifyTreasureResource> treasureStorage;

	@Static
	public Storage<String, ItemResource> itemStorage;

	@Static
	public Storage<Integer, WeekCriResource> weekCriStorage;

	@Static
	public Storage<String, CommonFirstPayResource> firstPayStorage;

	@Static
	public Storage<String, CommonTreasureActivityResource> commonTreasureStorage;

	@Static
	public Storage<String, CommonRedPacketResource> redPacketStorage;

	@Static
	public Storage<String, CommonCollectWordResource> collectWordStorage;

	@Static
	public Storage<String, CommonRecollectResource> recollectResources;

	@Static
	public Storage<String, LuckyDrawResource> luckyDrawStorage;

	@Static
	public Storage<String, CommonGoldTreasuryResource> goldTreasuryStorage;

	@Static
	public Storage<String, CommonConsumeGiftResource> consumeGiftStroage;

	@Static("COMMONACTIVITY:COMMON_CONSUME_TIME_CONDS")
	private ConfigValue<Map<String, ArrayList<String>>> COMMON_CONSUME_TIME_CONDS;

	public CoreConditions getConsumeTimeConds(String activeName) {
		if (COMMON_CONSUME_TIME_CONDS.getValue().containsKey(activeName)) {
			ArrayList<String> conds = COMMON_CONSUME_TIME_CONDS.getValue().get(activeName);
			return CoreConditionManager.getInstance().getCoreConditions(1, conds.toArray(new String[0]));
		}
		return null;
	}

	@Static("COMMONACTIVITY:SPECIAL_BOSS_REFRESH_NOTICE_TIME")
	public ConfigValue<Map<String, ArrayList<String>>> SPECIAL_BOSS_REFRESH_NOTICE_TIME;

	@Static("COMMONACTIVITY:SPECIAL_BOSSES_SPAWN_TIME")
	public ConfigValue<Map<String, ArrayList<String>>> SPECIAL_BOSSES_SPAWN_TIME;

	@Static("COMMONACTIVITY:IDENTIFY_TREASURE_RANK_NUM")
	public ConfigValue<Integer> IDENTIFY_TREASURE_RANK_NUM;

	@Static("COMMONACTIVITY:IDENTIFY_TREASURE_RANK_LITTLE_GIT_TREASURE")
	public ConfigValue<Integer> IDENTIFY_TREASURE_RANK_LITTLE_GIT_TREASURE;

	@Static("COMMONACTIVITY:WEEK_CRI_ACTIVITY_TIME_CONDS")
	public ConfigValue<String[]> WEEK_CRI_ACTIVITY_TIME_CONDS;

	public CoreConditions getWeekCriActivityTimeCond() {
		return CoreConditionManager.getInstance().getCoreConditions(1, WEEK_CRI_ACTIVITY_TIME_CONDS.getValue());
	}

	@Static("COMMONACTIVITY:WEEK_CRI_COUNT_ADD_CONDS")
	public ConfigValue<String[]> WEEK_CRI_COUNT_ADD_CONDS;

	public CoreConditions getWeekCriCountAddCond() {
		return CoreConditionManager.getInstance().getCoreConditions(1, WEEK_CRI_COUNT_ADD_CONDS.getValue());
	}

	@Static("COMMONACTIVITY:TREASUREACTVIE_TIME_CONDS")
	public ConfigValue<Map<String, ArrayList<String>>> TREASUREACTVIE_TIME_CONDS;

	public CoreConditions getTreasureActiveTimeConds(String activityName) {
		ArrayList<String> conds = TREASUREACTVIE_TIME_CONDS.getValue().get(activityName);
		if (conds != null) {
			return CoreConditionManager.getInstance().getCoreConditions(1, conds.toArray(new String[0]));
		}
		return null;

	}

	@Static("COMMONACTIVITY:COMMON_REDPACKET_TIME_CONDS")
	public ConfigValue<Map<String, ArrayList<String>>> COMMON_REDPACKET_TIME_CONDS;

	public CoreConditions getRedPacketTimeConds(String activityName) {
		ArrayList<String> conds = COMMON_REDPACKET_TIME_CONDS.getValue().get(activityName);
		if (conds != null) {
			return CoreConditionManager.getInstance().getCoreConditions(1, conds.toArray(new String[0]));
		}
		return null;

	}

	public Collection<CommonRecollectResource> getCurrentCommonRecollectResources() {
		return recollectResources.getIndex(CommonRecollectResource.ACTIVITYNAME, true);
	}

	public CoreConditions getRecollectLogDataConditions(RecollectType type) {
		CommonRecollectResource resource = getCurrentCommonRecollectResource(type);
		return resource == null ? null : resource.getLogDataConditions();
	}

	public CommonRecollectResource getCurrentCommonRecollectResource(RecollectType type) {
		// spd
		for (CommonRecollectResource resource : getCurrentCommonRecollectResources()) {
			if (resource.getEventType() == type) {
				return resource;
			}
		}
		return null;
	}

	public long currentCommonRecollectStartDayKey(RecollectType type) {
		CoreConditions conds = getCurrentCommonRecollectResource(type).getLogDataConditions();
		BetweenTimeCondition cond = conds.findConditionType(BetweenTimeCondition.class);
		return DayKey.valueOf(cond.getStartTime()).getLunchTime();
	}

	public long currentCommonRecollectEndDayKey(RecollectType type) {
		CoreConditions conds = getCurrentCommonRecollectResource(type).getLogDataConditions();
		BetweenTimeCondition cond = conds.findConditionType(BetweenTimeCondition.class);
		return DayKey.valueOf(cond.getEndTime()).getLunchTime();
	}

	public BetweenTimeCondition getTimeCondition() {
		return getRecollectRecievedConditions().findConditionType(BetweenTimeCondition.class);
	}

	public CoreConditions getRecollectRecievedConditions() {
		CommonRecollectResource resource = getCurrentCommonRecollectResources().iterator().next();
		return resource == null ? null : resource.getClawbackConditions();
	}

	public CoreActions getRecollectCopperActions(RecollectType type, int count) {
		if (count == 0) {
			return new CoreActions();
		}
		CommonRecollectResource resource = getCurrentCommonRecollectResource(type);
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(count, resource.getCopperActionId());
		return actions;
	}

	public CoreActions getRecollectGoldActions(RecollectType type, int count) {
		if (count == 0) {
			return new CoreActions();
		}
		CommonRecollectResource resource = getCurrentCommonRecollectResource(type);
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(count, resource.getGoldActionId());
		return actions;
	}

	public Reward getRecollectRewards(Player player, RecollectType type, int count) {
		CommonRecollectResource resource = getCurrentCommonRecollectResource(type);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, resource.getRewardGroupId());
		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(player));
		params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		params.put("STANDARD_COINS", PlayerManager.getInstance().getStandardCoins(player));
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, params);
		reward.mutipleRewards(count);
		return reward;
	}

	public CommonSPServerResource getResourceContainName(String name) {
		for (CommonSPServerResource resource : commonServerStorage.getAll()) {
			if (name.contains(resource.getName())) {
				return resource;
			}
		}
		return null;
	}

	public CommonGoldTreasuryResource getGoldTreasuryResource(String activeName, int groupId) {
		List<CommonGoldTreasuryResource> resources = goldTreasuryStorage.getIndex(
				CommonGoldTreasuryResource.ACTIVE_NAME, activeName);
		CommonGoldTreasuryResource goldTreasuryResource = null;
		for (CommonGoldTreasuryResource resource : resources) {
			if (resource.getGroupId() == groupId) {
				goldTreasuryResource = resource;
				break;
			}
		}
		return goldTreasuryResource;
	}

	public SubModuleType getGoldTreasuryActionSubType(int groupId, boolean isGold) {
		SubModuleType type = null;
		if (isGold) {
			switch (groupId) {
			case 1:
				type = SubModuleType.GOLD_TREASURY_GOLD_ACITION_1;
				break;
			case 2:
				type = SubModuleType.GOLD_TREASURY_GOLD_ACITION_2;
				break;
			case 3:
				type = SubModuleType.GOLD_TREASURY_GOLD_ACITION_3;
				break;
			case 4:
				type = SubModuleType.GOLD_TREASURY_GOLD_ACITION_4;
				break;
			default:
				break;
			}
		} else {
			switch (groupId) {
			case 1:
				type = SubModuleType.GOLD_TREASURY_ACITION_1;
				break;
			case 2:
				type = SubModuleType.GOLD_TREASURY_ACITION_2;
				break;
			case 3:
				type = SubModuleType.GOLD_TREASURY_ACITION_3;
				break;
			case 4:
				type = SubModuleType.GOLD_TREASURY_ACITION_4;
				break;
			default:
				break;
			}
		}
		return type;
	}

	public SubModuleType getGoldTreasuryRewardSubType(int groupId) {
		SubModuleType type = null;
		switch (groupId) {
		case 1:
			type = SubModuleType.GOLD_TREASURY_REWARD_1;
			break;
		case 2:
			type = SubModuleType.GOLD_TREASURY_REWARD_2;
			break;
		case 3:
			type = SubModuleType.GOLD_TREASURY_REWARD_3;
			break;
		case 4:
			type = SubModuleType.GOLD_TREASURY_REWARD_4;
			break;
		default:
			break;
		}
		return type;
	}
}
