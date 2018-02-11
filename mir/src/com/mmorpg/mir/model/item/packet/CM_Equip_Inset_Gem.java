package com.mmorpg.mir.model.item.packet;

public class CM_Equip_Inset_Gem {
	private long gemId;
	
	private long equipmentId;

	public long getGemId() {
		return gemId;
	}

	public void setGemId(long gemId) {
		this.gemId = gemId;
	}

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

}
