package com.mmorpg.mir.model.capturetown.model;

public enum ResultType {
	/** 被杀 失败 **/
	FAIL_BEEN_KILL(1),
	/** 超时 失败 **/
	FAIL_TIME_OUT(2),
	/** 挑战成功但是没得到城池 **/
	WIN_NOT_GAIN_TOWN(3),
	/** 挑战成功并得到城池 **/
	WIN_GAIN_TOWN(4);
	
	private final int value;
	
	private ResultType(int v) {
		this.value = v;
	}
	
	public final int getValue() {
		return value;
	}
	
}
