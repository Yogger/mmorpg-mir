package com.mmorpg.mir.model.lifegrid.packet;

import java.util.HashMap;
import java.util.Map;

public class SM_LifeGrid_Storage_Update {
	private int code;

	private int type;

	private HashMap<Integer, Object> storageUpdate;

	public static SM_LifeGrid_Storage_Update valueOf(int type, Map<Integer, Object> houseUpdate) {
		SM_LifeGrid_Storage_Update result = new SM_LifeGrid_Storage_Update();
		result.type = type;
		result.storageUpdate = new HashMap<Integer, Object>(houseUpdate);
		return result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

}
