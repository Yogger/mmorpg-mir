package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.item.storage.ItemStorage;

public class SM_Merge {
	private int code;

	private ItemStorage storage;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ItemStorage getStorage() {
		return storage;
	}

	public void setStorage(ItemStorage storage) {
		this.storage = storage;
	}

}
