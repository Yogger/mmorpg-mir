package com.mmorpg.mir.model.commonactivity.model;

public enum ClawbackStatus {
	MODULE_NOT_OPEN(0), CLAWBACKED(1), CAN_CLAW(2), CANNOT_CLAW(3);

	private final int value;

	private ClawbackStatus(int v) {
		this.value = v;
	}

	public final int getValue() {
		return value;
	}
}
