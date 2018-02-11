package com.mmorpg.mir.model.openactive;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
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
import com.mmorpg.mir.model.openactive.resource.OpenActiveEveryDayRechargeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveExpResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveHorseUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveSoulUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.PublicTestGiftResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class OpenActiveConfig {

	private static OpenActiveConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static OpenActiveConfig getInstance() {
		return INSTANCE;
	}

	@Static
	public Storage<String, OpenActiveExpResource> expStorage;

	@Static
	public Storage<String, OpenActiveEquipResource> equipStorage;

	@Static
	public Storage<String, OpenActiveEveryDayRechargeResource> everyDayRechargeStorage;

	@Static
	public Storage<String, OpenActiveHorseUpgradeResource> horseUpgradeStorage;

	@Static
	public Storage<String, OpenActiveArtifactResource> artifactActiveStorage;

	@Static
	public Storage<String, OpenActiveCompeteResource> competeActiveStorage;

	@Static
	public Storage<String, OpenActiveEnhanceResource> enhancePowerActiveStorage;

	@Static
	public Storage<String, OpenActiveCollectItemResource> collectItemStorage;

	@Static
	public Storage<String, OpenActiveSoulUpgradeResource> soulUpgradeStorage;

	@Static
	public Storage<String, OldOpenActiveSoulUpgradeResource> oldSoulUpgradeStorage;

	@Static
	public Storage<String, OldOpenActiveCompeteResource> oldCompeteActiveStorage;

	@Static
	public Storage<String, PublicTestGiftResource> giftResource;

	@Static
	public Storage<String, GroupPurchaseResource> groupPurchaseStorage;

	@Static
	public Storage<String, GroupPurchaseTwoResource> groupPurchaseTwoStorage;

	@Static
	public Storage<String, GroupPurchaseThreeResource> groupPurchaseThreeStorage;

	@Static("OPENACTIVE:CONSUME_ACTIVE_COND")
	public ConfigValue<String[]> CONSUME_ACTIVE_COND;

	@Static("OPENACTIVE:ACCESS_CONSUMERANK_COND")
	public ConfigValue<String[]> ACCESS_CONSUMERANK_COND;

	@Static("OPENACTIVE:MILITARY_ACTIVE_COND")
	public ConfigValue<String[]> MILITARY_ACTIVE_COND;

	@Static("OPENACTIVE:LEVEL_ACTIVE_COND")
	public ConfigValue<String[]> LEVEL_ACTIVE_COND;

	@Static("OPENACTIVE:ENHANCE_ACTIVE_DURATION")
	public ConfigValue<String[]> ENHANCE_ACTIVE_DURATION;

	@Static("OPENACTIVE:HORSE_ACTIVE_DURATION")
	public ConfigValue<String[]> HORSE_ACTIVE_DURATION;

	@Static("OPENACTIVE:ARTIFACT_ACTIVE_DURATION")
	public ConfigValue<String[]> ARTIFACT_ACTIVE_DURATION;

	@Static("OPENACTIVE:MILITARY_REWARD_LIMIT")
	public ConfigValue<String[]> MILITARY_REWARD_LIMIT;

	@Static("OPENACTIVE:LEVEL_REWARD_LIMIT")
	public ConfigValue<String[]> LEVEL_REWARD_LIMIT;

	@Static("OPENACTIVE:SERVER_RESOURCE_ID")
	public ConfigValue<Map<String, String>> SERVER_RESOURCE_ID;

	@Static("OPENACTIVE:SOUL_ACTIVE_DURATION")
	public ConfigValue<String[]> SOUL_ACTIVE_DURATION;

	@Static("OPENACTIVE:OLD_SOUL_ACTIVE_DURATION")
	public ConfigValue<String[]> OLD_SOUL_ACTIVE_DURATION;

	/** 每日充值一轮的档次 */
	@Static("OPENACTIVE:EVERYDAY_RECHARGE_GRADE")
	public ConfigValue<Integer> EVERYDAY_RECHARGE_GRADE;

	/** 每日充值没有领取奖励邮件标题il18n */
	@Static("OPENACTIVE:EVERYDAY_RECHARGE_MAIL_TITLE_IL18N")
	public ConfigValue<String> EVERYDAY_RECHARGE_MAIL_TITLE_IL18N;

	/** 每日充值没有领取奖励邮件内容 */
	@Static("OPENACTIVE:EVERYDAY_RECHARGE_MAIL_CONTENT_IL18N")
	public ConfigValue<String> EVERYDAY_RECHARGE_MAIL_CONTENT_IL18N;

	@Static("OPENACTIVE:EVERYDAY_RECHARGE_COND")
	private ConfigValue<String[]> EVERYDAY_RECHARGE_COND;

	private CoreConditions consumeActiveDuration;

	private CoreConditions consumeRankCond;

	public CoreConditions getAccessRankCond() {
		if (consumeRankCond == null) {
			consumeRankCond = CoreConditionManager.getInstance().getCoreConditions(1,
					ACCESS_CONSUMERANK_COND.getValue());
		}
		return consumeRankCond;
	}

	private CoreConditions artifactActiveDuration;

	private CoreConditions levelActiveDuration;

	private CoreConditions everydayActiveDuration;

	private CoreConditions horseActiveDuration;

	private CoreConditions soulActiveDuration;

	private CoreConditions oldSoulActiveDuration;

	public CoreConditions getSoulActiveDurationCond() {
		if (soulActiveDuration == null) {
			soulActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					SOUL_ACTIVE_DURATION.getValue());
		}
		return soulActiveDuration;
	}

	public CoreConditions getOldSoulActiveDurationCond() {
		if (oldSoulActiveDuration == null) {
			oldSoulActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					OLD_SOUL_ACTIVE_DURATION.getValue());
		}
		return oldSoulActiveDuration;
	}

	public CoreConditions getHorseActiveDurationCond() {
		if (horseActiveDuration == null) {
			horseActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					HORSE_ACTIVE_DURATION.getValue());
		}
		return horseActiveDuration;
	}

	public CoreConditions getConsumeDurationCondition() {
		if (consumeActiveDuration == null) {
			consumeActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					CONSUME_ACTIVE_COND.getValue());
		}
		return consumeActiveDuration;
	}

	public CoreConditions getLevelDurationCondition() {
		if (levelActiveDuration == null) {
			levelActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1, LEVEL_ACTIVE_COND.getValue());
		}
		return levelActiveDuration;
	}

	public CoreConditions getArtifactActiveDurationCond() {
		if (artifactActiveDuration == null) {
			artifactActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					ARTIFACT_ACTIVE_DURATION.getValue());
		}
		return artifactActiveDuration;
	}

	public CoreConditions getEveryDayRechargeDurationCond() {
		if (everydayActiveDuration == null) {
			everydayActiveDuration = CoreConditionManager.getInstance().getCoreConditions(1,
					EVERYDAY_RECHARGE_COND.getValue());
		}
		return everydayActiveDuration;
	}

	public List<OpenActiveCompeteResource> getSpecifiedRankTypeResources(int rankTypeValue) {
		List<OpenActiveCompeteResource> resources = competeActiveStorage.getIndex(
				OpenActiveCompeteResource.COMPETE_RANK_TYPE, rankTypeValue);
		return resources;
	}

	public List<OpenActiveCompeteResource> getInstanceRecieveResource(int rankTypeValue) {
		List<OpenActiveCompeteResource> resources = New.arrayList();
		for (OpenActiveCompeteResource resource : getSpecifiedRankTypeResources(rankTypeValue)) {
			if (resource.isInstanceCanRecieve()) {
				resources.add(resource);
			}
		}
		return resources;
	}

	public OldOpenActiveCompeteResource getOldSpecifiedRankTypeResource(int rankTypeValue) {
		return getOldSpecifiedRankTypeResources(rankTypeValue).get(0);
	}

	public List<OldOpenActiveCompeteResource> getOldSpecifiedRankTypeResources(int rankTypeValue) {
		List<OldOpenActiveCompeteResource> resources = oldCompeteActiveStorage.getIndex(
				OldOpenActiveCompeteResource.COMPETE_RANK_TYPE, rankTypeValue);
		return resources;
	}

	public List<OldOpenActiveCompeteResource> getOldInstanceRecieveResource(int rankTypeValue) {
		List<OldOpenActiveCompeteResource> resources = New.arrayList();
		for (OldOpenActiveCompeteResource resource : getOldSpecifiedRankTypeResources(rankTypeValue)) {
			if (resource.isInstanceCanRecieve()) {
				resources.add(resource);
			}
		}
		return resources;
	}

	public OpenActiveCompeteResource getSpecifiedRankTypeResource(int rankTypeValue) {
		return getSpecifiedRankTypeResources(rankTypeValue).get(0);
	}

	/** 星级套装开服活动 */
	@Static("OPENACTIVE:STARITEM_REWARD_CONDS")
	private ConfigValue<String[]> STARITEM_REWARD_CONDS;

	private CoreConditions starItemRewardConds;

	public CoreConditions getStarItemRewardConds() {
		if (starItemRewardConds == null) {
			starItemRewardConds = CoreConditionManager.getInstance().getCoreConditions(1,
					STARITEM_REWARD_CONDS.getValue());
		}
		return starItemRewardConds;
	}

	/** 星级套装开服活动奖励 */
	@Static("OPENACTIVE:STARITEM_REWARDID")
	public ConfigValue<String> STARITEM_REWARDID;

	@Static("OPENACTIVE:STARITEM_REWARD_MAIL_TITLE_I18N")
	public ConfigValue<String> STARITEM_REWARD_MAIL_TITLE_I18N;

	@Static("OPENACTIVE:STARITEM_REWARD_MAIL_CONTENT_I18N")
	public ConfigValue<String> STARITEM_REWARD_MAIL_CONTENT_I18N;

	/** 国旗争夺奖励 */
	@Static("OPENACTIVE:COUNTRYFLAG_REWARDID")
	public ConfigValue<String> COUNTRYFLAG_REWARDID;

	@Static("OPENACTIVE:COUNTRYFLAG_REWARD_MAIL_TITLE_I18N")
	public ConfigValue<String> COUNTRYFLAG_REWARD_MAIL_TITLE_I18N;

	@Static("OPENACTIVE:COUNTRYFLAG_REWARD_MAIL_CONTENT_18N")
	public ConfigValue<String> COUNTRYFLAG_REWARD_MAIL_CONTENT_18N;

	@Static("OPENACTIVE:COUNTRYFLAG_REWARD_CONDS")
	private ConfigValue<String[]> COUNTRYFLAG_REWARD_CONDS;

	private CoreConditions countryFlagRewardConds;

	public CoreConditions getCountryFlagRewardConds() {
		if (countryFlagRewardConds == null) {
			countryFlagRewardConds = CoreConditionManager.getInstance().getCoreConditions(1,
					COUNTRYFLAG_REWARD_CONDS.getValue());
		}
		return countryFlagRewardConds;
	}

	@Static("OPENACTIVE:COUNTRYFLAG_FINISH_COUNT")
	public ConfigValue<Integer> COUNTRYFLAG_FINISH_COUNT;

	@Static("OPENACTIVE:COUNTRYCOPY_OPENACTIVE_CONDS")
	private ConfigValue<String[]> COUNTRYCOPY_OPENACTIVE_CONDS;

	private CoreConditions coutryCopyActiveConds;

	public CoreConditions getCountryCopyActiveConds() {
		if (coutryCopyActiveConds == null) {
			coutryCopyActiveConds = CoreConditionManager.getInstance().getCoreConditions(1,
					COUNTRYCOPY_OPENACTIVE_CONDS.getValue());
		}
		return coutryCopyActiveConds;
	}

	@Static("OPENACTIVE:COUNTRYCOPY_OPENACTIVE_ADDCOUNT")
	public ConfigValue<Integer> COUNTRYCOPY_OPENACTIVE_ADDCOUNT;

	public CoreConditions getEnhancePowerLogConditions() {
		for (OpenActiveEnhanceResource resource : enhancePowerActiveStorage.getAll()) {
			return resource.getLogDateConditions();
		}
		return new CoreConditions();
	}

	@Static("PUBLICTEST:SPECIAL_BOSSES")
	public ConfigValue<Map<String, String>> SPECIAL_BOSSES;

	@Static("PUBLICTEST:SPECIAL_BOSSES_SPAWN_TIME")
	public ConfigValue<String[]> SPECIAL_BOSSES_SPAWN_TIME;

	@Static("PUBLISTEST:SPECIAL_BOSS_NOTICE_I18N")
	public ConfigValue<String> SPECIAL_BOSS_NOTICE_I18N;

	@Static("PUBLISTEST:SPECIAL_BOSS_NOTICE_CHANNEL")
	public ConfigValue<Integer> SPECIAL_BOSS_NOTICE_CHANNEL;

	@Static("PUBLICTEST:SPECIAL_BOSS_REWARD")
	public ConfigValue<Map<String, String>> SPECIAL_BOSS_REWARD;

	@Static("PUBLICTEST:SPECIAL_BOSS_REWARD_MAIL_TITLE")
	public ConfigValue<String> SPECIAL_BOSS_REWARD_MAIL_TITLE;

	@Static("PUBLICTEST:SPECIAL_BOSS_REWARD_MAIL_CONTENT")
	public ConfigValue<String> SPECIAL_BOSS_REWARD_MAIL_CONTENT;

	@Static("PUBLICTEST:SPECIAL_BOSS_ACT_TIME")
	public ConfigValue<String[]> SPECIAL_BOSS_ACT_TIME;

	@Static("PUBLICTEST:SPECIAL_BOSS_REFRESH_NOTICE_TIME")
	public ConfigValue<String[]> SPECIAL_BOSS_REFRESH_NOTICE_TIME;

	private CoreConditions publicTestTimeConditions;

	public CoreConditions getPublicTestConditions() {
		if (publicTestTimeConditions == null) {
			publicTestTimeConditions = CoreConditionManager.getInstance().getCoreConditions(1,
					SPECIAL_BOSS_ACT_TIME.getValue());
		}
		return publicTestTimeConditions;
	}

	@Static("PUBLICTEST:DOUBLE_ELEVEN_CEREMONY_TIME")
	public ConfigValue<String[]> DOUBLE_ELEVEN_CEREMONY_TIME;

	public CoreConditions doubleElevenCeremonyTime;

	public CoreConditions getDoubleElementCeremonyTime() {
		if (doubleElevenCeremonyTime == null) {
			doubleElevenCeremonyTime = CoreConditionManager.getInstance().getCoreConditions(1,
					DOUBLE_ELEVEN_CEREMONY_TIME.getValue());
		}
		return doubleElevenCeremonyTime;
	}

	@Static("PUBLICTEST:GROUPPURCHASE_TIME_CONDS")
	private ConfigValue<String> GROUPPURCHASE_TIME_CONDS;

	private CoreConditions groupPurchaseTimeConds;

	public CoreConditions getGroupPurchaseTimeConds() {
		if (groupPurchaseTimeConds == null) {
			groupPurchaseTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUPPURCHASE_TIME_CONDS.getValue());
		}
		return groupPurchaseTimeConds;
	}

	@Static("PUBLICTEST:GROUPPURCHASE_REWARDMAIL_TIME_CONDS")
	private ConfigValue<String> GROUPPURCHASE_REWARDMAIL_TIME_CONDS;

	@Static("PUBLICTEST:DOUBLE_11_VERSION")
	public ConfigValue<Integer> DOUBLE_11_VERSION;

	private CoreConditions groupPurchaseMailTimeConds;

	public CoreConditions getGroupPurchaseMailTimeConds() {
		if (groupPurchaseMailTimeConds == null) {
			groupPurchaseMailTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUPPURCHASE_REWARDMAIL_TIME_CONDS.getValue());
		}
		return groupPurchaseMailTimeConds;
	}

	/**
	 * 
	 * @param groupId
	 * @param giftResources
	 * @return
	 */
	public PublicTestGiftResource getLowestInGruop(int groupId) {
		for (PublicTestGiftResource resource : getPublicTestResources()) {
			if (resource.getVersion() != DOUBLE_11_VERSION.getValue()) {
				continue;
			}
			if (resource.getGroupId() != groupId) {
				continue;
			}
			if (resource.getLowLevelId() == null) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * 返回组的个数
	 * 
	 * @param giftResources
	 * @return
	 */
	public int getPublicTestGiftGroupNum() {
		int groupNum = 0;
		for (PublicTestGiftResource gr : getPublicTestResources()) {
			if (gr.getVersion() != DOUBLE_11_VERSION.getValue()) {
				continue;
			}
			if (gr.getGroupId() > groupNum) {
				groupNum = gr.getGroupId();
			}
		}
		return groupNum;
	}

	public Collection<PublicTestGiftResource> getPublicTestResources() {
		return giftResource.getAll();
	}

	@Static("OPENACTIVE:GROUP_PURCHASE_TWO_TIME_CONDS")
	private ConfigValue<String[]> GROUP_PURCHASE_TWO_TIME_CONDS;
	private CoreConditions groupPurchaseTwoTimeConds;

	public CoreConditions getGroupPurchaseTwoTimeConds() {
		if (null == groupPurchaseTwoTimeConds) {
			groupPurchaseTwoTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUP_PURCHASE_TWO_TIME_CONDS.getValue());
		}
		return groupPurchaseTwoTimeConds;
	}

	@Static("OPENACTIVE:GROUPPURCHASE_TWO_REWARDMAIL_TIME_CONDS")
	private ConfigValue<String[]> GROUPPURCHASE_TWO_REWARDMAIL_TIME_CONDS;

	private CoreConditions groupPurchaseTwoMailTimeConds;

	public CoreConditions getGroupPurchaseTwoMailTimeConds() {
		if (groupPurchaseTwoMailTimeConds == null) {
			groupPurchaseTwoMailTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUPPURCHASE_TWO_REWARDMAIL_TIME_CONDS.getValue());
		}
		return groupPurchaseTwoMailTimeConds;
	}

	@Static("OPENACTIVE:GROUP_PURCHASE_THREE_TIME_CONDS")
	private ConfigValue<String[]> GROUP_PURCHASE_THREE_TIME_CONDS;

	private CoreConditions groupPurchaseThreeTimeConds;

	public CoreConditions getGroupPurchaseThreeTimeConds() {
		if (null == groupPurchaseThreeTimeConds) {
			groupPurchaseThreeTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUP_PURCHASE_THREE_TIME_CONDS.getValue());
		}
		return groupPurchaseThreeTimeConds;
	}

	@Static("OPENACTIVE:GROUPPURCHASE_THREE_REWARDMAIL_TIME_CONDS")
	private ConfigValue<String[]> GROUPPURCHASE_THREE_REWARDMAIL_TIME_CONDS;

	private CoreConditions groupPurchaseThreeMailTimeConds;

	public CoreConditions getGroupPurchaseThreeMailTimeConds() {
		if (groupPurchaseThreeMailTimeConds == null) {
			groupPurchaseThreeMailTimeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					GROUPPURCHASE_THREE_REWARDMAIL_TIME_CONDS.getValue());
		}
		return groupPurchaseThreeMailTimeConds;
	}

	@Static("OPENACTIVE:RECHARGE_MINIMAL_GOLD_REQUIRED")
	public ConfigValue<Integer> RECHARGE_MINIMAL_GOLD_REQUIRED;

	@Static("OPENACTIVE:RECHARGE_CELEBRATE_COMPENSATE_MAIL_TITLE")
	public ConfigValue<String> RECHARGE_CELEBRATE_COMPENSATE_MAIL_TITLE;

	@Static("OPENACTIVE:RECHARGE_CELEBRATE_COMPENSATE_MAIL_CONTENT")
	public ConfigValue<String> RECHARGE_CELEBRATE_COMPENSATE_MAIL_CONTENT;

	@Static("OPENACTIVE:RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP")
	public ConfigValue<String> RECHARGE_CELEBRATE_REWARD_CHOOSERGROUP;

	@Static("OPENACTIVE:CELEBRATE_DOUBLE_EXP_RATE")
	public ConfigValue<Float> CELEBRATE_DOUBLE_EXP_RATE;

	@Static("OPENACTIVE:CELEBRATE_DOUBLE_EXP_CONDS")
	private ConfigValue<String[]> CELEBRATE_DOUBLE_EXP_CONDS;

	private CoreConditions celebrateConds;

	public CoreConditions getCelebrateConds() {
		if (null == celebrateConds) {
			celebrateConds = CoreConditionManager.getInstance().getCoreConditions(1,
					CELEBRATE_DOUBLE_EXP_CONDS.getValue());
		}
		return celebrateConds;
	}

	@Static("OPENACTIVE:CELEBRATE_FIRWORK_ACTIONS")
	private ConfigValue<String[]> CELEBRATE_FIRWORK_ACTIONS;

	public CoreActions getFireworkActions(int count) {
		return CoreActionManager.getInstance().getCoreActions(count, CELEBRATE_FIRWORK_ACTIONS.getValue());
	}

	@Static("OPENACTIVE:CELEBRATE_FIREWORK_ATTEND_CONDS")
	private ConfigValue<String[]> CELEBRATE_FIREWORK_ATTEND_CONDS;

	private CoreConditions fireworkAttendConds;

	public CoreConditions getFireworkAttendConds() {
		if (null == fireworkAttendConds) {
			fireworkAttendConds = CoreConditionManager.getInstance().getCoreConditions(1,
					CELEBRATE_FIREWORK_ATTEND_CONDS.getValue());
		}
		return fireworkAttendConds;
	}

	@Static("OPENACTIVE:CELEBRATE_FIREWORK_REWARDIDS")
	public ConfigValue<String[]> CELEBRATE_FIREWORK_REWARDIDS;

	@Static("OPENACTIVE:SPECIAL_BOSS_REWARD_COND")
	public ConfigValue<String[]> SPECIAL_BOSS_REWARD_COND;

	private CoreConditions specialBossRewardCond;

	public CoreConditions getSpecialBossRewardCondition() {
		if (specialBossRewardCond == null) {
			specialBossRewardCond = CoreConditionManager.getInstance().getCoreConditions(1,
					SPECIAL_BOSS_REWARD_COND.getValue());
		}
		return specialBossRewardCond;
	}

}
