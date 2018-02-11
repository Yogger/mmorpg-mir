package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_UnEquip {
	private int code;
	private Map<Integer, Object> packUpdate;
	private Map<Integer, Object> equipUpdate;

	private int equipStorageType;

	public static SM_UnEquip valueOf(Player player, EquipmentStorageType equipStorageType) {
		SM_UnEquip result = new SM_UnEquip();
		result.packUpdate = player.getPack().collectUpdate();
		result.equipUpdate = equipStorageType.getEquipmentStorage(player).collectUpdate();
		result.equipStorageType = equipStorageType.getWhere();
		return result;
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

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
