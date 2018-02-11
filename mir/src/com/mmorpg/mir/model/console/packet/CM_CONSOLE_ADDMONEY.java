package com.mmorpg.mir.model.console.packet;

public class CM_CONSOLE_ADDMONEY {
	private int type;
	private int value;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
