package com.mmorpg.mir.model.openactive.model;

public enum ActivityEnum {
	/** 双倍经验 */
	DOUBLE_EXP(1),
	/** 炫耀烟花 */
	FIREWORK(2),
	/** 绝世boss */
	BOSS(3),
	/** 充值活动 */
	RECHARGE(4);

	private int value;

	public static ActivityEnum typeOf(int v) {
		for (ActivityEnum e : ActivityEnum.values()) {
			if (e.getValue() == v) {
				return e;
			}
		}
		throw new IllegalArgumentException("非法活动类型" + v);
	}

	private ActivityEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
