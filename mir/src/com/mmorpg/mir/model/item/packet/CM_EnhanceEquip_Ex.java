package com.mmorpg.mir.model.item.packet;

public class CM_EnhanceEquip_Ex {
	private int equipIndex; // 强化装备的部位 type.ordinal
	private boolean gold;
	private int equipStorageType;

	public int getEquipIndex() {
		return equipIndex;
	}

	public boolean isGold() {
		return gold;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipIndex(int equipIndex) {
		this.equipIndex = equipIndex;
	}

	public void setGold(boolean gold) {
		this.gold = gold;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
