package com.mmorpg.mir.model.capturetown.model;

public enum TownType {
	
	/** PVE城池(全民城池) **/
	PVE(1),
	/** PVP城池 **/
	PVP(2);
	
	private final int value;
	
	private TownType(int v) {
		this.value = v;
	}
	
	public final int getValue() {
		return value;
	}
	
}
