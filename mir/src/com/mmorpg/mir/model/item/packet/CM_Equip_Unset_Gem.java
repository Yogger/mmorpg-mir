package com.mmorpg.mir.model.item.packet;

public class CM_Equip_Unset_Gem {

	private long equipmentId;
	
	private int index;

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
