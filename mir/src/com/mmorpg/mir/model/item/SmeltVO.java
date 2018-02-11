package com.mmorpg.mir.model.item;

import com.mmorpg.mir.model.chat.model.show.object.ItemShow;

public class SmeltVO {

	private ItemShow key;
	private int smeltValue;

	public static SmeltVO valueOf(ItemShow key, int v) {
		SmeltVO vo = new SmeltVO();
		vo.key = key;
		vo.smeltValue = v;
		return vo;
	}

	public ItemShow getKey() {
		return key;
	}

	public void setKey(ItemShow key) {
		this.key = key;
	}

	public int getSmeltValue() {
		return smeltValue;
	}

	public void setSmeltValue(int smeltValue) {
		this.smeltValue = smeltValue;
	}

}
