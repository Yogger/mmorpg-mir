package com.mmorpg.mir.model.reward.model;

import com.mmorpg.mir.model.purse.model.CurrencyType;

public enum RewardType {
	/** 经验(code:无意义) */
	EXP("EXP"),
	/** 流通货币(code:{@link CurrencyType}) */
	CURRENCY("CURRENCY"),
	/** 道具/装备(code:道具ID) */
	ITEM("ITEM"),
	/** DP */
	DP("DP"),
	/** HP */
	HP("HP"),
	/** MP **/
	MP("MP"),
	/** 坐骑 */
	HORSE("HORSE"),
	/** 英魂 */
	SOUL("SOUL"),
	/** 神兵 */
	ARTIFACT("ARTIFACT"),
	/** 技能 */
	SKILL("SKILL"),
	/** 人品 */
	RP("RP"),
	/** 国家仓库 */
	COUNTRY_CURRENCY("COUNTRY_CURRENCY"),
	/** 对玩家释放一个BUFF */
	BUFF("BUFF"),
	/** 等级 */
	LEVEL("LEVEL"),
	/** 临时VIP */
	TEMPLE_VIP("TEMPLE_VIP"),
	/** 战魂 */
	COMBAT_SPIRIT("COMBAT_SPIRIT"),
	/** 熔炼值 */
	SMELT("SMELT"),
	/** 足迹 */
	FOOTPRINT("FOOTPRINT"),
	/** vip */
	VIP("VIP"),
	/** 坐骑幻化 */
	HORSE_ILLUTION("HORSE_ILLUTION"),
	/** 随机游历任务 */
	RANDOM_QUEST("RANDOM_QUEST"),
	/** 称号 */
	NICKNAME("NICKNAME"),
	/** 成就点 */
	ACHIEVEMENT_POINT("ACHIEVEMENT_POINT"),
	/** 坐骑道具卡 */
	HORSEITEM("HORSEITEM"),
	/** 神兵buff */
	ARTIFACT_BUFF("ARTIFACT_BUFF"),
	/** 活跃值 */
	ACTIVEVALUE("ACTIVEVALUE"),
	/** 国家建设值 */
	COUNTRY_BUILDVALUE("COUNTRY_BUILDVALUE"),
	/** 激活武魂 */
	ACTIVE_EQUIPSOUL("ACTIVE_EQUIPSOUL"),
	/** 祝福值 */
	BLESSING_VALUE("BLESSING_VALUE"),
	/** 坐骑强化 */
	HORSE_ENHANCE("HORSE_ENHANCE"),
	/** 时装 */
	FASHION("FASHION"),
	/** 衣橱经验 */
	FASHION_EXP("FASHION_EXP"),
	/** 坐骑技能 */
	HORSE_SKILL("HORSE_SKILL"),
	/** 英魂强化 */
	SOUL_ENHANCE("HORSE_ENHANCE"),
	/** 神兵强化 */
	ARTIFACT_ENHANCE("HORSE_ENHANCE"),
	/** 全身强化加1 */
	EQUIPMENTS_ENHANCE("EQUIPMENTS_ENHANCE"),
	/** 美人技能 */
	BEAUTY_SKILL("BEAUTY_SKILL"),
	/** 兵书技能 */
	WARBOOK_SKILL("WARBOOK_SKILL"),
	/** 兵书进阶丹 */
	WARBOOK_UPCOUNT("WARBOOK_UPCOUNT"),
	/** 兵书强化丹 */
	WARBOOK_ITEM("WARBOOK_ENHANCE"),
	/** 美人道具 */
	BEAUTY_ITEM("BEAUTY_ITEM"),
	/** 命格碎片 */
	LIFEGRID_POINT("LIFEGRID_POINT"),
	/** 命格 */
	LIFEGRID("LIFEGRID"),
		/** 印玺道具  */
	SEAL_ITEM("SEAL_ITEM"),
	/** 印玺技能  */
	SEAL_SKILL("SEAL_SKILL"),
	/** 坐骑成长丹 */
	HORSE_GROW_ITEM("HORSE_GROW_ITEM"),
	/** 神兵成长丹 */
	ARTIFACT_GROW_ITEM("ARTIFACT_GROW_ITEM"),
	/** 英魂成长丹 */
	SOUL_GROW_ITEM("SOUL_GROW_ITEM"), ;

	private final String value;

	private RewardType(String value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public String value() {
		return this.value;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return this.value;
	}

	/**
	 * Return the enum constant of this type with the specified numeric value.
	 * 
	 * @param statusCode
	 *            the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException
	 *             if this enum has no constant for the specified numeric value
	 */
	public static RewardType typeOf(String name) {
		for (RewardType status : values()) {
			if (status.name().equals(name)) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + name + "]");
	}
}
