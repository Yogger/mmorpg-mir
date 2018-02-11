package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Equip_Create_Soul {

	private int code;

	private Map<Integer, Object> packUpdate;
	private Map<Integer, Object> equipUpdate;
	
	public static SM_Equip_Create_Soul valueOf(Player player) {
		SM_Equip_Create_Soul sm = new SM_Equip_Create_Soul();
		sm.packUpdate = player.getPack().collectUpdate();
		sm.equipUpdate = player.getEquipmentStorage().collectUpdate();
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public Map<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(Map<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

}
