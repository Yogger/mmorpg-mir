package com.mmorpg.mir.model.quest.model;

/**
 * 章节类型
 * 
 * @author Kuang Hao
 * @since v1.0 2013-2-21
 * 
 */
public enum KeyType {
	/** 限时时间内登录次数 */
	LOGIN_DAY("LOGIN_DAY"),
	/** 杀怪 */
	MONSTER_HUNT("MONSTER_HUNT"),
	/** 杀怪获得道具 */
	MONSTER_HUNT_ITEM("MONSTER_HUNT_ITEM"),
	/** 护镖 **/
	GUARD("GUARD"),
	/** 采集 */
	GATHER("GATHER"),
	/** 任务怪死亡 */
	QUEST_MONSTER_DIE("QUEST_MONSTER_DIE"),
	/** 运镖 */
	EXPRESS("EXPRESS"),
	/** 劫镖 */
	EXPRESS_ROB("EXPRESS_ROB"),
	/** 完成任务 */
	COMPLETE_QUEST("COMPLETE_QUEST"),
	/** 完成任务或 */
	COMPLETE_QUEST_OR("COMPLETE_QUEST_OR"),
	/** 杀敌国玩家任务 */
	KILL_PLAYER("KILL_PLAYER"),
	/** 杀正在搬砖的玩家 */
	KILL_BRICK_PLAYER("KILL_BRICK_PLAYER"),
	/** 搬砖 */
	BRICK("BRICK"),
	/** 刺探 */
	INVESTIGATE("INVESTIGATE"),
	/** 操练 */
	EXERCISE("EXERCISE"),
	/** 继承装备 */
	EXTENDS_EQUIPMENT("EXTENDS_EQUIPMENT"),
	/** 总共营救的次数 */
	RESCUE_ALL("RESCUE_ALL"),
	/** 总共刺探的次数 */
	INVESTIGATE_ALL("INVESTIGATE_ALL"),
	/** 总共运镖的次数 */
	EXPRESS_ALL("EXPRESS_ALL"),
	/** 总共搬砖的次数 */
	BRICK_ALL("BRICK_ALL"),
	/** 穿戴了几件激活了的装备 */
	SOULED_EQUIPMENT("SOULED_EQUIPMENT"),
	/** 总共国家祭祀的次数 */
	FETE_ALL("FETE_ALL"),
	/** 杀精英 */
	ELITE("ELITE"),
	/** 收集过物品 */
	COLLECT_ITEM("COLLECT_ITEM"),
	/** 中立地图杀怪 */
	NEUTRALITY("NEUTRALITY"),
	/** 熔炼次数 */
	SMELT("SMELT"),
	/** 杀BOSS */
	BOSS_HUNT("BOSS_HUNT"),

	TARGET_MONSTER_HUNT("TARGET_MONSTER_HUNT");

	private final String value;

	private KeyType(String value) {
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
	public static KeyType valueOf(int statusCode) {
		for (KeyType status : values()) {
			if (status.value.equals(statusCode + "")) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}
}
