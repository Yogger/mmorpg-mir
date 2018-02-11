package com.mmorpg.mir.model.item.storage;

import com.mmorpg.mir.model.item.AbstractItem;

public class RemoveItem {
	private AbstractItem item;
	private int num;

	public AbstractItem getItem() {
		return item;
	}

	public void setItem(AbstractItem item) {
		this.item = item;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public static RemoveItem valueOf(AbstractItem item, int num) {
		RemoveItem result = new RemoveItem();
		result.item = item;
		result.num = num;
		return result;
	}
}
