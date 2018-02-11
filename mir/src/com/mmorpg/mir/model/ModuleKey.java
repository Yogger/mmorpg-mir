package com.mmorpg.mir.model;

/**
 * 模块编号,定义自己的模块编号
 * 
 * @author Kuang Hao
 * @since v1.0 2013-2-20
 * 
 */
public enum ModuleKey {

	/** 全部模块，提供给属性系统使用 */
	ALL(0),
	/** 装备 */
	EQUIPMENT(1),
	/** 钱包 */
	PURSE(2),
	/** 状态管理 */
	COMPLEXSTATE(3),
	/** 背包 */
	PACKAGE(4),
	/** VIP */
	VIP(5),
	/** 生命 */
	LIFE(6),
	/** 商店 */
	SHOP(7),
	/** 坐骑 */
	HORSE(8),
	/** 任务 */
	QUEST(9),
	/** 副本 */
	COPY(10),
	/** 帮会 */
	GANG(11),
	/** 技能 */
	SKILL(12),
	/** 运镖 */
	EXPRESS(13),
	/** 刺探 */
	INVESTIGATE(14),
	/** 人品 */
	RP(15),
	/** 太庙 */
	TEMPLE(16),
	/** 福利 */
	WELFARE(17),
	/** 军衔 */
	MILITARY(18),
	/** 仓库 */
	WAREHOUSE(19),
	/** 冷却 **/
	COOLDOWN(20),
	/** 位置信息 **/
	POSITION(21),
	/** effect管理器 */
	EFFECTCONTROLL(22),
	/** 英魂 */
	SOUL_PF(23),
	/** 神兵 */
	ARTIFACT(24),
	/** 国家相关信息 */
	COUNTRY_PLAYER_INFO(25),
	/** 玩家排行榜信息 */
	RANK_INFO(26),
	/** 模块开启信息 */
	MODULE_OPEN_INFO(27),
	/** 战魂 */
	COMBAT_SPIRIT(28),
	/** 掉落 */
	DROP(29),
	/** 防沉迷 */
	ANTIADDICATION(30),
	/** 转职 */
	PROMOTION(31),
	/** 营救 */
	RESCUE(32),
	/** 足迹 */
	FOOTPRINT(33),
	/** 膜拜 */
	WARSHIP(34),
	/** 平台数据 */
	OPERATOR(35),
	/** 称号 */
	NICKNAME(36),
	/** 投资理财 */
	INVEST(37),
	/** 开服活动 */
	OPENACTIVE(38),
	/** 储君 */
	RESERVEKING(39),
	/** 收集 */
	COLLECT(40),
	/** 探宝 */
	TREASURE(41),
	/** 国家副本 */
	COUNTRYCOPY(42),
	/** 成就 */
	// ACHIEVEMENT(43),
	/** 国家科技 */
	COUNTRYTECHNOLOGY(44),
	/** 时装 */
	FASHION(45),
	/** 和服活动 */
	MERGEACTIVE(46),
	/** 黑市 */
	BLACKSHOP(47),
	/** 以后所有的公共活动 */
	COMMON_ACTIVITY(48),
	/** 西周王陵 */
	GASCOPY(49),
	/** BOSS */
	BOSS(50),
	/** 聚宝盆投资 */
	INVESTAGATE(51),
	/** 美人 */
	BEAUTY(52),
	/** 兵书 */
	WARBOOK(53),
	/** 军衔兵法 */
	MILITARY_STRATEGY(54),
	/** 坐骑装备栏 */
	HORSE_EQUIPSTORE(55),
	/** 跨服 */
	TRANSFER(56),
	/** 命格 */
	LIFEGRID(57),
	/** 转生 */
	SUICIDE(58),
	/** 装备转生 */
	EQUIP_SUICIDE_TURN(59),
	/** 点将 */
	POINTGENERAL(60),
	/** 印玺 */
	SEAL(61),
	/** 统计数据 */
	STAT(1000), ;

	private final int value;

	private ModuleKey(int value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return value;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
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
	public static ModuleKey valueOf(int statusCode) {
		for (ModuleKey status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}
}
