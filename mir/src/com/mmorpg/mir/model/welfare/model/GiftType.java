package com.mmorpg.mir.model.welfare.model;

public enum GiftType {
	/** 等级礼包 **/
	LEVEL(1);
	
	private final int value;
	
	private GiftType(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return value;
	}
}
