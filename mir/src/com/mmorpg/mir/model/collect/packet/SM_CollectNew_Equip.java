package com.mmorpg.mir.model.collect.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;

public class SM_CollectNew_Equip {
		
	private Map<Integer, Equipment> equipCollect;

	public static SM_CollectNew_Equip valueOf(Player player) {
		SM_CollectNew_Equip sm = new SM_CollectNew_Equip();
		sm.equipCollect = new HashMap<Integer, Equipment>(player.getCollect().getCollectEquipLog());
		return sm;
	}
	
	public Map<Integer, Equipment> getEquipCollect() {
		return equipCollect;
	}

	public void setEquipCollect(Map<Integer, Equipment> equipCollect) {
		this.equipCollect = equipCollect;
	}
	
}
