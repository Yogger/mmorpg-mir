package com.mmorpg.mir.model.suicide.model;

public enum SuicideElementType {
	// 金
	GOLD(1),
	// 木
	WOOD(2),
	// 水
	WATER(3),
	// 火
	FIRE(4),
	// 土
	SOIL(5);

	private int value;

	private SuicideElementType(int v) {
		this.value = v;
	}

	public static SuicideElementType typeOf(int v) {
		for (SuicideElementType type : SuicideElementType.values()) {
			if (type.value == v) {
				return type;
			}
		}
		throw new RuntimeException("非法元素类型[" + v + "]");
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
