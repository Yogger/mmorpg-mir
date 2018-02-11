package com.mmorpg.mir.model.operator.packet;

public class CM_Operator_ForbidChat {
	private String name;
	private int minuteTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinuteTime() {
		return minuteTime;
	}

	public void setMinuteTime(int minuteTime) {
		this.minuteTime = minuteTime;
	}

}
