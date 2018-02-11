package com.mmorpg.mir.utils;

import com.mmorpg.mir.model.item.resource.ItemResource;

public class ItemResourceJsonItem {
	private String key;
	private String itemType;
	private String name;

	public static ItemResourceJsonItem valueOf(ItemResource ir) {
		ItemResourceJsonItem irji = new ItemResourceJsonItem();
		irji.setKey(ir.getKey());
		irji.setItemType(ir.getItemType().name());
		irji.setName(ir.getOperatorName());
		return irji;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
