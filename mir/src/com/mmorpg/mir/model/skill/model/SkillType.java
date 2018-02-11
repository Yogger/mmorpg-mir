package com.mmorpg.mir.model.skill.model;

public enum SkillType {
	/** 主动技能 */
	CAST(1),
	/** 被动技能 */
	PASSIVE(2),
	// 策划说这样分类
	// ?
	PVE(3),
	// ?
	PVP(4);

	private int value;

	private SkillType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}

}
