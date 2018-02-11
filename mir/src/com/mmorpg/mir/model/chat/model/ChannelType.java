package com.mmorpg.mir.model.chat.model;

public enum ChannelType {
	// /** 系统 */
	// SYSTEM(0),
	/** 全服 */
	ALL(1),
	/** 家族 */
	GANG(2),
	/** 国家 */
	COUNTRY(3),
	/** 本地(同地图) */
	LOCAL(4),
	/** 玩家附近(可视范围) */
	CANSEE(5),
	/** 队伍 */
	GROUP(6),
	/** 私人 */
	PRIVATE(7),
	/** 本地指定线(同地图) */
	LOCAL_LINE(8),
	/** 本地(同地图) */
	LOCAL_COUNTRY(9);

	private int value;

	private ChannelType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}
}
