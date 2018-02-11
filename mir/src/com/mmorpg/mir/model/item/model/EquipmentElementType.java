package com.mmorpg.mir.model.item.model;

public enum EquipmentElementType {
	METAL(1), WOOD(2), WATER(3), FIRE(4), EARTH(5);

	private int value;

	public static EquipmentElementType valueOf(int code) {
		for (EquipmentElementType id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of EquimentElementType[" + code + "]");
	}

	private EquipmentElementType(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
	public static void main(String[] args) {
		int a = EquipmentElementType.valueOf(1).getValue();
		System.out.println(a);
	}
}
