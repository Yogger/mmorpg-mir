package com.mmorpg.mir.model.chat.model;

public enum ShowType {
	/** 展示道具 */
	ITEM(1),
	/** 展示坐标 */
	POSITION(2);

	private int value;

	public static ShowType valueOf(int code) {
		for (ShowType id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private ShowType(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
