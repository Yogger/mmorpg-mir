package com.mmorpg.mir.model.mergeactive;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.mergeactive.resource.MergeCheapGiftBagResource;
import com.mmorpg.mir.model.mergeactive.resource.MergeConsumeCompeteResource;
import com.mmorpg.mir.model.mergeactive.resource.MergeLoginGiftResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class MergeActiveConfig {
	private static MergeActiveConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static MergeActiveConfig getInstance() {
		return INSTANCE;
	}

	@Static
	public Storage<String, MergeLoginGiftResource> giftResource;

	@Static
	public Storage<String, MergeCheapGiftBagResource> cheapGiftBagResource;
	
	@Static
	public Storage<String, MergeConsumeCompeteResource> mergeConsumeCompeteResources;

	@Static("MERGEACTIVE:OPEN_DOUBLE_EXP_CONDITION")
	public ConfigValue<String[]> OPEN_DOUBLE_EXP_CONDITION;

	@Static("MERGEACTIVE:OPEN_DOUBLE_EXP_BASEVALUE")
	public ConfigValue<Integer> OPEN_DOUBLE_EXP_BASEVALUE;

	@Static("MERGEACTIVE:TEMPLE_AND_EXPRESS_OPEN_CONDITION")
	public ConfigValue<String[]> TEMPLE_AND_EXPRESS_OPEN_CONDITION;

	@Static("MERGEACTIVE:TEMPLE_OPEN_TIME")
	public ConfigValue<String[]> TEMPLE_OPEN_TIME;

	@Static("MERGEACTIVE:EXPRESS_OPEN_TIME")
	public ConfigValue<String[]> EXPRESS_OPEN_TIME;

	@Static("MERGEACTIVE:TEMPLE_AND_EXPRESS_OPEN_DATES")
	public ConfigValue<Integer[]> TEMPLE_AND_EXPRESS_OPEN_DATES;

	@Static("MERGEACTIVE:TEMPLE_AND_EXPRESS_BROADCAST_AHEAD_MINUTES")
	public ConfigValue<Integer> TEMPLE_AND_EXPRESS_BROADCAST_AHEAD_MINUTES;

	private CoreConditions openDoubleExpConditions;

	private CoreConditions templeAndExpressOpenConditions;

	public CoreConditions getOpenDoubleExpConditions() {
		if (openDoubleExpConditions == null) {
			openDoubleExpConditions = CoreConditionManager.getInstance().getCoreConditions(1,
					OPEN_DOUBLE_EXP_CONDITION.getValue());
		}
		return openDoubleExpConditions;
	}

	public CoreConditions getTempleAndExpressOpenConditions() {
		if (templeAndExpressOpenConditions == null) {
			templeAndExpressOpenConditions = CoreConditionManager.getInstance().getCoreConditions(1,
					TEMPLE_AND_EXPRESS_OPEN_CONDITION.getValue());
		}
		return templeAndExpressOpenConditions;
	}

	public int getMergeCheapGiftBagGroupNum() {
		int groupNum = 0;
		for (MergeCheapGiftBagResource bagResource : cheapGiftBagResource.getAll()) {
			if (bagResource.getGroupId() > groupNum) {
				groupNum = bagResource.getGroupId();
			}
		}
		return groupNum;
	}
	
	public MergeConsumeCompeteResource getConsumeCompeteResource(String key) {
		return mergeConsumeCompeteResources.get(key, true);
	}

	public MergeConsumeCompeteResource getConsumeActiveResource() {
		return mergeConsumeCompeteResources.getAll().iterator().next();
	}
	

	// 战胜家族族长奖励
	@Static("MERGEACTIVE:MERGE_GOW_WIN_GANG_LEADER_REWARDID")
	public ConfigValue<String> MERGE_GOW_WIN_GANG_LEADER_REWARDID;

	@Static("MERGEACTIVE:MERGE_GOW_WIN_GAND_LEADER_MAIL_TITLE_IL18NID")
	public ConfigValue<String> MERGE_GOW_WIN_GAND_LEADER_MAIL_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_GOW_WIN_GAND_LEADER_MAIL_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_GOW_WIN_GAND_LEADER_MAIL_CONTENT_IL18NID;

	// 战胜家族成员合服活动奖励
	@Static("MERGEACTIVE:MERGE_GOW_WIN_GANG_MEMBER_REWARDID")
	public ConfigValue<String> MERGE_GOW_WIN_GANG_MEMBER_REWARDID;

	@Static("MERGEACTIVE:MERGE_GOW_WIN_GAND_MEMBER_MAIL_TITLE_IL18NID")
	public ConfigValue<String> MERGE_GOW_WIN_GAND_MEMBER_MAIL_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_GOW_WIN_GAND_MEMBER_MAIL_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_GOW_WIN_GAND_MEMBER_MAIL_CONTENT_IL18NID;

	// 失败家族合服活动奖励
	@Static("MERGEACTIVE:MERGE_GOW_LOSE_GANG_MEMBER_REWARDID")
	public ConfigValue<String> MERGE_GOW_LOSE_GANG_MEMBER_REWARDID;

	@Static("MERGEACTIVE:MERGE_GOW_LOSE_GAND_MEMBER_MAIL_TITLE_IL18NID")
	public ConfigValue<String> MERGE_GOW_LOSE_GAND_MEMBER_MAIL_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_GOW_LOSE_GAND_MEMBER_MAIL_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_GOW_LOSE_GAND_MEMBER_MAIL_CONTENT_IL18NID;

	// 和服活动皇城争霸
	@Static("MERGEACTIVE:MERGE_KOW_KING_REWARDID")
	public ConfigValue<String> MERGE_KOW_KING_REWARDID;

	@Static("MERGEACTIVE:MERGE_KOW_KING_TITLE_IL18NID")
	public ConfigValue<String> MERGE_KOW_KING_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_KOW_KING_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_KOW_KING_CONTENT_IL18NID;

	@Static("MERGEACTIVE:MERGE_KOW_WIN_EXCEPT_KING_REWARDID")
	public ConfigValue<String> MERGE_KOW_WIN_EXCEPT_KING_REWARDID;

	@Static("MERGEACTIVE:MERGE_KOW_WIN_EXCEPT_KING_TITLE_IL18NID")
	public ConfigValue<String> MERGE_KOW_WIN_EXCEPT_KING_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_KOW_WIN_EXCEPT_KING_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_KOW_WIN_EXCEPT_KING_CONTENT_IL18NID;

	@Static("MERGEACTIVE:MERGE_KOW_LOSE_REWARDID")
	public ConfigValue<String> MERGE_KOW_LOSE_REWARDID;

	@Static("MERGEACTIVE:MERGE_KOW_LOSE_TITLE_IL18NID")
	public ConfigValue<String> MERGE_KOW_LOSE_TITLE_IL18NID;

	@Static("MERGEACTIVE:MERGE_KOW_LOSE_CONTENT_IL18NID")
	public ConfigValue<String> MERGE_KOW_LOSE_CONTENT_IL18NID;

}
