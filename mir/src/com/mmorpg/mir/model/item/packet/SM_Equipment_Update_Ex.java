package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_Equipment_Update_Ex {
	private int code;
	private Equipment equip;
	private Map<String, Integer> luckyPoints;
	private Map<String, Long> luckyPointsTime;
	private int addLucky;
	private int equipStorageType;

	public static SM_Equipment_Update_Ex failReturn(Map<String, Integer> luckyPoints,
			Map<String, Long> luckyPointsTime, int code, int lucky, EquipmentStorageType equipStorageType) {
		SM_Equipment_Update_Ex ret = new SM_Equipment_Update_Ex();
		ret.code = code;
		ret.luckyPoints = luckyPoints;
		ret.luckyPointsTime = luckyPointsTime;
		ret.addLucky = lucky;
		ret.equipStorageType = equipStorageType.getWhere();
		return ret;
	}

	public static SM_Equipment_Update_Ex valueOf(Map<String, Integer> luckyPoints, Map<String, Long> luckyPointsTime,
			Equipment equip, EquipmentStorageType equipStorageType) {
		SM_Equipment_Update_Ex ret = new SM_Equipment_Update_Ex();
		ret.luckyPoints = luckyPoints;
		ret.luckyPointsTime = luckyPointsTime;
		ret.equip = equip;
		ret.equipStorageType = equipStorageType.getWhere();
		return ret;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Equipment getEquip() {
		return equip;
	}

	public void setEquip(Equipment equip) {
		this.equip = equip;
	}

	public Map<String, Integer> getLuckyPoints() {
		return luckyPoints;
	}

	public void setLuckyPoints(Map<String, Integer> luckyPoints) {
		this.luckyPoints = luckyPoints;
	}

	public Map<String, Long> getLuckyPointsTime() {
		return luckyPointsTime;
	}

	public void setLuckyPointsTime(Map<String, Long> luckyPointsTime) {
		this.luckyPointsTime = luckyPointsTime;
	}

	public int getAddLucky() {
		return addLucky;
	}

	public void setAddLucky(int addLucky) {
		this.addLucky = addLucky;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
