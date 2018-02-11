package com.mmorpg.mir.model.moduleopen.model;

public enum ModuleOpenFactorType {
	/** 未知原因*/
	ALL(0),
	/** 玩家等级升级*/
	LEVELUP(1),
	/** 军衔等级升级*/
	MILITARYRANKUP(2),
	/** 完成任务*/
	QUESTCOMPLETE(3);
	
	private final int value;
	
	ModuleOpenFactorType(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return value;
	}
}
