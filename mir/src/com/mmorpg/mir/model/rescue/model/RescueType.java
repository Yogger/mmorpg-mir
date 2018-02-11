package com.mmorpg.mir.model.rescue.model;

public enum RescueType {
	/** 采集 */
	GATHER(1),
	/** 对话 */
	CHAT(2),
	/** 杀怪 */
	MONSTER(3);

	private int value;

	private RescueType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}
}
