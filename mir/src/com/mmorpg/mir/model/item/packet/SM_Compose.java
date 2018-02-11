package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.item.Equipment;

public class SM_Compose {

	private Equipment equipment;

	public static SM_Compose valueOf(Equipment equipment) {
		SM_Compose c = new SM_Compose();
		c.equipment = equipment;
		return c;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

}
