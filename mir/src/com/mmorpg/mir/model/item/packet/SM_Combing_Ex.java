package com.mmorpg.mir.model.item.packet;

import java.util.HashMap;
import java.util.Map;

public class SM_Combing_Ex {
	private int code;
	private HashMap<Integer, Object> packUpdate;
	private HashMap<Integer, Object> equipUpdate;

	public static SM_Combing_Ex valueOf(Map<Integer, Object> packUpdate, Map<Integer, Object> equipUpdate) {
		SM_Combing_Ex result = new SM_Combing_Ex();
		result.code = 0;
		result.packUpdate = new HashMap<Integer, Object>(packUpdate);
		result.equipUpdate = new HashMap<Integer, Object>(equipUpdate);
		return result;
	}

	public static SM_Combing_Ex valueOf() {
		SM_Combing_Ex result = new SM_Combing_Ex();
		result.code = 1;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(HashMap<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

	public HashMap<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(HashMap<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

}
