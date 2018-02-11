package com.mmorpg.mir.model.lifegrid.packet;

import java.util.Map;

public class SM_LifeGrid_Take {

	private int code;
	private Map<Integer, Object> lifePackUpdate;
	private Map<Integer, Object> lifeStorageUpdate;

	public static SM_LifeGrid_Take valueOf(Map<Integer, Object> lifePackUpdate, Map<Integer, Object> lifeStorageUpdate) {
		SM_LifeGrid_Take result = new SM_LifeGrid_Take();
		result.lifePackUpdate = lifePackUpdate;
		result.lifeStorageUpdate = lifeStorageUpdate;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getLifePackUpdate() {
		return lifePackUpdate;
	}

	public void setLifePackUpdate(Map<Integer, Object> lifePackUpdate) {
		this.lifePackUpdate = lifePackUpdate;
	}

	public Map<Integer, Object> getLifeStorageUpdate() {
		return lifeStorageUpdate;
	}

	public void setLifeStorageUpdate(Map<Integer, Object> lifeStorageUpdate) {
		this.lifeStorageUpdate = lifeStorageUpdate;
	}

}
