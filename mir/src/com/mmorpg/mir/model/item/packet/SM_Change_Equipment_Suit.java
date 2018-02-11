package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Change_Equipment_Suit {

	private Map<Integer, Object> equipUpdate;

	private Map<Integer, Object> packUpdate;

	public static SM_Change_Equipment_Suit valueOf(Player player) {
		SM_Change_Equipment_Suit sm = new SM_Change_Equipment_Suit();
		sm.equipUpdate = player.getEquipmentStorage().collectUpdate();
		sm.packUpdate = player.getPack().collectUpdate();
		return sm;
	}

	public Map<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(Map<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}
	
}
