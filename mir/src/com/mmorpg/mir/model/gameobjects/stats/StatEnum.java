package com.mmorpg.mir.model.gameobjects.stats;

/**
 * 
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-5
 * 
 */
public enum StatEnum {

	/** 最大气血 */
	MAXHP(0, "MAXHP"),
	/** 最大内力 */
	MAXMP(1, "MAXMP"),
	/** 物理攻击 */
	PHYSICAL_ATTACK(2, "PHYSICAL_ATTACK"),
	/** 物理防御 */
	PHYSICAL_DEFENSE(3, "PHYSICAL_DEFENSE"),
	/** 策略攻击 */
	MAGICAL_ATTACK(4, "MAGICAL_ATTACK"),
	/** 魔法防御 */
	MAGICAL_DEFENSE(5, "MAGICAL_DEFENSE"),
	/** 暴击 0-10000 */
	CRITICAL(6, "CRITICAL"),
	/** 暴击抵抗 0-10000 */
	CRITICAL_RESIST(7, "CRITICAL_RESIST"),
	/** 移动速度 */
	SPEED(8, "SPEED"),
	/** 破甲 0-10000 */
	IGNORE(9, "IGNORE"),
	/** 破甲抵抗 0-10000 */
	IGNORE_RESIST(10, "IGNORE_RESIST"),
	/** 伤害减免 0-10000 */
	DAMAGE_REDUCE(11, "DAMAGE_REDUCE"),
	/** 伤害增加 0-10000 */
	DAMAGE_INCREASE(12, "DAMAGE_INCREASE"),
	/** 真实伤害 */
	TRUE_DAMAGE(13, "TRUE_DAMAGE"),
	/** 真实减伤 */
	TRUE_DEFENCE(14, "TRUE_DEFENCE"),
	/** 急速 */
	HASTE(15, "HASTE"),
	/** 回血量 **/
	REGEN_HP(16, "REGEN_HP"),
	/** 回蓝量 **/
	REGEN_MP(17, "REGEN_MP"),
	/** 经验的增益 **/
	EXP_PLUS(18, "EXP_PLUS"),
	/** RP值的增益 */
	RP_PLUS(19, "RP_PLUS"),
	/** 物理免伤 */
	PHYSICAL_RESIST(20, "PHYSICAL_RESIST"),
	/** 魔法免伤 */
	MAGICAL_RESIST(21, "MAGICAL_RESIST"),
	/** 吸血 */
	LIFE_STEAL(22, "LIFE_STEAL"),
	/** 反伤 */
	RETURN_DAMAGE(23, "RETURN_DAMAGE"),
	/** 忽略护甲 */
	IGNORE_DEFENSE(24, "IGNORE_DEFENSE"),
	/** 增强护甲 */
	INCREASE_DEFENSE(25, "INCREASE_DEFENSE"),
	/** 最大怒气值 */
	MAXDP(26, "MAXDP"),
	/** 怒气值的回复 */
	DP_INCREMENT(27, "DP_INCREMENT"),
	/** 多倍荣誉丹 */
	HONOR_PLUS(28, "HONOR_PLUS"),
	/** 防护 */
	BARRIER(29, "BARRIER"),
	/** 防护吸收率0-10000 */
	BARRIER_PERCENT(30, "BARRIER_PERCENT"),
	/** 防护值5回值,0-10000 */
	BARRIER_RESTORE(31, "BARRIER_RESTORE"),
	/** 增加暴击伤害百分比 */
	CRITICAL_DAMAGE_INCREASE(32, "CRITICAL_DAMAGE_INCREASE"),
	/** 减少暴击伤害百分比 */
	CRITICAL_DAMAGE_REDUCE(33, "CRITICAL_DAMAGE_REDUCE"),
	/** 伤害增加 0-10000 */
	DAMAGE_INCREASE1(34, "DAMAGE_INCREASE1"),
	/** 伤害减免 0-10000 */
	DAMAGE_REDUCE1(35, "DAMAGE_REDUCE1");

	private final int value;

	private final String name;

	private StatEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return (byte) value;
	}

	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return String.valueOf(name);
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
	public static StatEnum valueOf(int statusCode) {
		for (StatEnum status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

	public String getName() {
		return name;
	}
}
