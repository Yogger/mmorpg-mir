package com.mmorpg.mir.model.country.model.log;

/**
 * 日志记录
 * 
 * @author 37wan
 * 
 */
public enum CountryLogEnum {

	/** 拿 */
	TAKE(1),
	/** 放 */
	PUT(2),
	/** 移动 */
	MOVE(3);
	private final int action;

	private CountryLogEnum(int action) {
		this.action = action;
	}

	public int getAction() {
		return action;
	}

}
