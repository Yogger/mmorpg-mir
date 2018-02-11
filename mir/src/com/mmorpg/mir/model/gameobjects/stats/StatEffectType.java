package com.mmorpg.mir.model.gameobjects.stats;

public enum StatEffectType {
	/** 基础等级 */
	LEVEL_BASE(1),
	/** 技能效果(被动技能) */
	SKILL_EFFECT(2),
	/** 装备道具效果 */
	ITEM_EFFECT(3),
	/** 技能效果 */
	BUFF_EFFECT(4),
	/** 坐骑 */
	HORSE(5),
	/** 人品的效果 */
	RP_EFFECT(6),
	/** 军衔 */
	MILITARY(7),
	/** 装备额外 */
	EQUIPMENT(8),
	/** 英魂 */
	SOUL(9),
	/** 神兵 */
	ARTIFACT(10),
	/** 背包,仓库 */
	PACK(11),
	/** 军衔兵法 */
	MILITARY_STRATEGY(12),
	/** 国家 */
	COUNTRY(13),
	/** VIP */
	VIP(14),
	/** 福利 */
	WELFARE(15),
	/** 战魂 */
	COMBAT_SPIRIT(16),
	/** 皇帝 */
	KINGOFKING(17),
	/** 将魂 */
	GENERAL_SPIRIT(18),
	/** 转职状态 */
	PROMOTION(19),
	/** 足迹 */
	FOOTPRINT(20),
	/** 称号 */
	NICKNAME(21),
	/** 众志成城buff */
	UNITY_BUFF(22),
	/** 国家副本助威 */
	COUNTRYCOPY_ENCOURAGE(23),
	// /** 成就 */
	// ACHIEVEMENT(22)
	FAMED_GENERAL_STATID(24),
	/** 时装 */
	FASHION(25),
	/** BOSS积分 */
	BOSS_COINS(26),
	/** 美人 */
	BEAUTY_GIRL(27),
	/** 兵书 */
	WARBOOK(28),
	/** 转生 */
	SUICIDE(29), 
	/** 印玺 */
	SEAL(30);

	private int value;

	private StatEffectType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
