package com.mmorpg.mir.model.reward.model.sample;

/**
 * 奖励配置表的上下文关键字
 * 
 * @author Kuang Hao
 * @since v1.0 2012-6-15
 * 
 */
public enum ContextKey {
	/** 等级 */
	LEVEL("LEVEL"),
	/** 英雄上阵人数 */
	FORMATION_HERO_SIZE("FORMATION_HERO_SIZE"),
	/** 聚义条件 */
	HERO_SAMPLE_ID("HERO_SAMPLE_ID"),
	/** 纹身品质 */
	TATTOO_QUALITY("TATTOO_QUALITY"),
	/** 纹身等级 */
	TATTOO_LEVEL("TATTOO_LEVEL"),
	/** boss活动id */
	BOSS_ID("BOSS_ID"),
	/** 元宝修炼武魂 */
	GOLD_PRACTICE_GANG("GOLD_PRACTICE_GANG");

	private final String value;

	private ContextKey(String value) {
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
	public static ContextKey valueOf(int statusCode) {
		for (ContextKey status : values()) {
			if (status.value.equals(statusCode + "")) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

}
