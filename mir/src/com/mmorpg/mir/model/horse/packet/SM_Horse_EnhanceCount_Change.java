package com.mmorpg.mir.model.horse.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.horse.model.Horse;

public class SM_Horse_EnhanceCount_Change {

	private HashMap<String, Integer> enhanceItemCount;

	public static SM_Horse_EnhanceCount_Change valueOf(Horse horse) {
		SM_Horse_EnhanceCount_Change result = new SM_Horse_EnhanceCount_Change();
		result.enhanceItemCount = new HashMap<String, Integer>(horse.getEnhanceItemCount());
		return result;
	}

	public HashMap<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(HashMap<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

}
