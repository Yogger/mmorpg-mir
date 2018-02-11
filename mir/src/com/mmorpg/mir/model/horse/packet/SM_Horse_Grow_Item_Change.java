package com.mmorpg.mir.model.horse.packet;

import java.util.Map;

import com.mmorpg.mir.model.horse.model.Horse;

public class SM_Horse_Grow_Item_Change {
	private Map<String, Integer> growItemCount;

	public static SM_Horse_Grow_Item_Change valueOf(Horse horse) {
		SM_Horse_Grow_Item_Change result = new SM_Horse_Grow_Item_Change();
		result.growItemCount = horse.getGrowItemCount();
		return result;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

}
