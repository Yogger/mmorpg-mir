package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_UnEquip_Change {
	private long playerId;
	private String key;
	private int equipStorageType;

	public static SM_UnEquip_Change valueOf(long playerId, String key, EquipmentStorageType equipStorageType) {
		SM_UnEquip_Change sec = new SM_UnEquip_Change();
		sec.setKey(key);
		sec.setPlayerId(playerId);
		sec.equipStorageType = equipStorageType.getWhere();
		return sec;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
