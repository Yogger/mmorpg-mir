package com.mmorpg.mir.model.beauty.packet;

import java.util.HashMap;
import java.util.Map;

public class SM_Beauty_Item_Change {
	private HashMap<String, Integer> itemCounts;

	public static SM_Beauty_Item_Change valueOf(Map<String, Integer> itemCounts) {
		SM_Beauty_Item_Change result = new SM_Beauty_Item_Change();
		result.itemCounts = new HashMap<String, Integer>(itemCounts);
		return result;
	}

	public HashMap<String, Integer> getItemCounts() {
		return itemCounts;
	}

	public void setItemCounts(HashMap<String, Integer> itemCounts) {
		this.itemCounts = itemCounts;
	}
}
