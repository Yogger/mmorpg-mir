package com.mmorpg.mir.model.gang.model;

/**
 * 帮会中的职位
 * 
 * @author zxy
 * 
 */
public enum GangPosition {

	/** 帮主 */
	Master(1),
	/** 副帮主 */
	Assistant(2),
	/** 帮众 */
	Member(3);

	private final int value;

	public static GangPosition valueOf(int statusCode) {
		for (GangPosition status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

	public int getValue() {
		return value;
	}

	private GangPosition(int value) {
		this.value = value;
	}

}
