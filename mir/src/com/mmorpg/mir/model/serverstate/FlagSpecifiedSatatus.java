package com.mmorpg.mir.model.serverstate;

public enum FlagSpecifiedSatatus {
	/** 没有计算过国家实力 **/
	NOT_CALCALCULATE(0),
	/** 计算过国家实力，但还没有进行第一次特殊任务筛选 **/
	CALCULATE(1),
	/** 初始化过第一次评价后的特殊任务筛选 **/
	INIT_FIRSTFLAG(2);
	
	private final int value;
	
	private FlagSpecifiedSatatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
