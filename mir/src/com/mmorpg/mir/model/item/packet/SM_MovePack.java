package com.mmorpg.mir.model.item.packet;

import java.util.Map;

public class SM_MovePack {

	private int code;

	private Map<Integer, Object> packUpdate;
	private Map<Integer, Object> wareHouseUpdate;

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

	public Map<Integer, Object> getWareHouseUpdate() {
		return wareHouseUpdate;
	}

	public void setWareHouseUpdate(Map<Integer, Object> wareHouseUpdate) {
		this.wareHouseUpdate = wareHouseUpdate;
	}

}
