package com.mmorpg.mir.model.lifegrid.packet;

import com.mmorpg.mir.model.item.LifeGridItem;

public class SM_LifeGrid_OperateLock {
	private int code;
	private int type;
	private int index;
	private LifeGridItem item;

	public static SM_LifeGrid_OperateLock valueOf(int type, int index, LifeGridItem item) {
		SM_LifeGrid_OperateLock result = new SM_LifeGrid_OperateLock();
		result.type = type;
		result.index = index;
		result.item = item;
		return result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public LifeGridItem getItem() {
		return item;
	}

	public void setItem(LifeGridItem item) {
		this.item = item;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
