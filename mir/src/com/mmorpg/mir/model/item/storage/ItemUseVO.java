package com.mmorpg.mir.model.item.storage;

import java.util.HashMap;
import java.util.Map;

public class ItemUseVO {
	private Map<String, Integer> itemDailyMap;
	
	public static ItemUseVO valueOf(Map<String, Integer> map) {
		ItemUseVO use = new ItemUseVO();
		use.itemDailyMap = new HashMap<String, Integer>(map);
		return use;
	}

	public Map<String, Integer> getItemDailyMap() {
		return itemDailyMap;
	}

	public void setItemDailyMap(Map<String, Integer> itemDailyMap) {
		this.itemDailyMap = itemDailyMap;
	}
	
}
