package com.mmorpg.mir.model.soul.packet;

import java.util.Map;

import com.mmorpg.mir.model.soul.model.Soul;

public class SM_Soul_Grow_Item_Change {
	private Map<String, Integer> growItemCount;

	public static SM_Soul_Grow_Item_Change valueOf(Soul soul) {
		SM_Soul_Grow_Item_Change result = new SM_Soul_Grow_Item_Change();
		result.growItemCount = soul.getGrowItemCount();
		return result;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}
}
