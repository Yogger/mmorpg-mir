package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.item.Equipment;

public class SM_EquipReElement {
	
	private Equipment equip;
	
	public static SM_EquipReElement valueOf(Equipment equip) {
		SM_EquipReElement sm = new SM_EquipReElement();
		sm.equip = equip;
		return sm;
	}

	public Equipment getEquip() {
		return equip;
	}

	public void setEquip(Equipment equip) {
		this.equip = equip;
	}
	
}
