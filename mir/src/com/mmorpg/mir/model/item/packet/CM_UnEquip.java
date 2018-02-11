package com.mmorpg.mir.model.item.packet;

public class CM_UnEquip {
	private int index;
	private int equipStorageType;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}
}
