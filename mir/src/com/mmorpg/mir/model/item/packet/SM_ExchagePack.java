package com.mmorpg.mir.model.item.packet;

import java.util.Map;

public class SM_ExchagePack {
	private int code;

	private Map<Integer, Object> packUpdate;

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

}
