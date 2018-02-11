package com.mmorpg.mir.model.item.model;

public enum EquipmentStatType {
	/** 极品属性 */
	PERFECT_STAT(1),
	/** 灵魂属性 */
	SOUL_STAT(2),
	/** 五行属性 */
	ELEMENT_STAT(3),
	/** 星级套装属性 */
	STAR_STAT(4),
	/** 灵魂套装属性 */
	SOUL_SUIT_STAT(5),
	/** 铸魂属性 */
	CREATE_SOUL_STAT(6),
	/** 宝石属性 */
	GEM_STAT(7),
	/** 宝石集合属性 */
	GEM_GATHER_STAT(8),
	/** 神装属性 */
	GOD_STAT(9),
	/** 普通套装属性 */
	SUIT_STAT(10),
	/** 普通属性 */
	COMMON_STAT(11),
	/** 转生属性 */
	SUICIDE_TURN(12);

	private int value;

	public static EquipmentStatType valueOf(int code) {
		for (EquipmentStatType id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of EquimentStatType[" + code + "]");
	}

	private EquipmentStatType(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}

}
