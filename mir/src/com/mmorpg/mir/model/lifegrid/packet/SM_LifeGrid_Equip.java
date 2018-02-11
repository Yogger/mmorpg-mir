package com.mmorpg.mir.model.lifegrid.packet;

import java.util.HashMap;
import java.util.Map;

public class SM_LifeGrid_Equip {
	private int code;
	private HashMap<Integer, Object> packUpdate;
	private HashMap<Integer, Object> equipUpdate;

	public static SM_LifeGrid_Equip valueOf(Map<Integer, Object> packUpdate, Map<Integer, Object> equipUpdate) {
		SM_LifeGrid_Equip result = new SM_LifeGrid_Equip();
		result.packUpdate = new HashMap<Integer, Object>(packUpdate);
		result.equipUpdate = new HashMap<Integer, Object>(equipUpdate);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HashMap<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(HashMap<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public HashMap<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(HashMap<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

}
