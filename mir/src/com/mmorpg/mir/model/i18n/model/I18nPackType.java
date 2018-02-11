package com.mmorpg.mir.model.i18n.model;

public enum I18nPackType {
	/** 字符串 */
	STRING((byte) 0),
	/** 玩家信息 */
	PLAYERSIMPLE((byte) 1),
	/** 系统道具信息 */
	SYSTEMITEM((byte) 2),
	/** 玩家道具信息 */
	PLAYERITEM((byte) 3),
	/** 玩家的位置信息 */
	POSITION((byte) 4),
	/** 玩家的足迹 */
	FOOTPRINT((byte) 5),
	/** 飞鞋坐标 */
	FLY_POSITION((byte) 6),
	/** 昨日英雄榜 */
	HERO_RANK((byte)7);

	private byte value;

	public static I18nPackType valueOf(byte code) {
		for (I18nPackType id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private I18nPackType(byte code) {
		this.value = code;
	}

	public byte getValue() {
		return this.value;
	}
}
