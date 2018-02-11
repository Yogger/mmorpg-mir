package com.mmorpg.mir.model.player.model;

public enum LoginType {
	/** 默认  */
	DEFUALT(1),
	/** 微端  */
	MINICLIENT(2);
	
	private final int value;
	
	LoginType(int v) {
		this.value = v;
    }
	
	public int getValue() {
		return value;
	}
}
