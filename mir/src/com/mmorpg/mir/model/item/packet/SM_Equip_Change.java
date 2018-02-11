package com.mmorpg.mir.model.item.packet;

public class SM_Equip_Change {
	private long playerId;
	private String key;
	private int equipStorageType;

	public static SM_Equip_Change valueOf(long playerId, String key, int equipStorageType) {
		SM_Equip_Change sec = new SM_Equip_Change();
		sec.setKey(key);
		sec.setPlayerId(playerId);
		sec.equipStorageType = equipStorageType;
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
