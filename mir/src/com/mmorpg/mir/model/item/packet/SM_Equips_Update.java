package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_Equips_Update {

	private String resultKey;
	private Map<Integer, Object> equipUpdate;
	private Map<Integer, Object> packUpdate;

	private int equipStorageType;

	public static SM_Equips_Update valueOf(Player player, String key, EquipmentStorageType equipStorageType) {
		SM_Equips_Update update = new SM_Equips_Update();
		update.equipUpdate = equipStorageType.getEquipmentStorage(player).collectUpdate();
		update.packUpdate = player.getPack().collectUpdate();
		update.resultKey = key;
		return update;
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

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
