package com.mmorpg.mir.model.exchange.core;

import com.mmorpg.mir.model.item.AbstractItem;

public class ExchangeItem {
	private int oldIndex = -1;
	private int index;
	private AbstractItem item;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public AbstractItem getItem() {
		return item;
	}

	public void setItem(AbstractItem item) {
		this.item = item;
	}

	public int getOldIndex() {
		return oldIndex;
	}

	public void setOldIndex(int oldIndex) {
		this.oldIndex = oldIndex;
	}

	public static ExchangeItem valueOf(int index, AbstractItem item) {
		return valueOf(-1, index, item);
	}

	public static ExchangeItem valueOf(int oldIndex, int index, AbstractItem item) {
		ExchangeItem result = new ExchangeItem();
		result.oldIndex = oldIndex;
		result.index = index;
		result.item = item;
		return result;
	}

}
