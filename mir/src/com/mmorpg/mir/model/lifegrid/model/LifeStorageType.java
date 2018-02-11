package com.mmorpg.mir.model.lifegrid.model;

public enum LifeStorageType {
	EQUIP(0), PACK(1), HOUSE(2);

	private int type;

	private LifeStorageType(int t) {
		this.type = t;
	}

	public static LifeStorageType typeOf(int t) {
		for (LifeStorageType type : LifeStorageType.values()) {
			if (type.getType() == t) {
				return type;
			}
		}
		throw new IllegalArgumentException("非法参数！");
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
