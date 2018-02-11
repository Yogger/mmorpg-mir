package com.mmorpg.mir.model.lifegrid.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;

public class SM_LifeGrid_DevourAll {
	private int code;

	private int type;
	private HashMap<Integer, Object> storageUpdate;

	public static SM_LifeGrid_DevourAll valueOf(LifeStorageType type, Map<Integer, Object> storageUpdate) {
		SM_LifeGrid_DevourAll result = new SM_LifeGrid_DevourAll();
		result.type = type.getType();
		result.storageUpdate = new HashMap<Integer, Object>(storageUpdate);
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HashMap<Integer, Object> getStorageUpdate() {
		return storageUpdate;
	}

	public void setStorageUpdate(HashMap<Integer, Object> storageUpdate) {
		this.storageUpdate = storageUpdate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
