package com.mmorpg.mir.model.item.packet;

public class CM_EnhanceEquip {

	private int equipIndex; //强化装备的部位 type.ordinal
	private boolean gold;
	
	public static CM_EnhanceEquip valueOf(int index) {
		CM_EnhanceEquip eh = new CM_EnhanceEquip();
		eh.equipIndex = index;
		return eh;
	}

	public boolean isGold() {
		return gold;
	}

	public void setGold(boolean gold) {
		this.gold = gold;
	}

	public int getEquipIndex() {
		return equipIndex;
	}

	public void setEquipIndex(int equipIndex) {
		this.equipIndex = equipIndex;
	}
}
