package com.mmorpg.mir.model.seal.packet;

import java.util.Map;

import com.mmorpg.mir.model.seal.model.Seal;

public class SM_Seal_Item_Change {

	private Map<String, Integer> itemCount;

	public static SM_Seal_Item_Change valueOf(Seal seal) {
		SM_Seal_Item_Change result = new SM_Seal_Item_Change();
		result.itemCount = seal.getItemCount();
		return result;
	}

	public Map<String, Integer> getItemCount() {
		return itemCount;
	}

	public void setItemCount(Map<String, Integer> itemCount) {
		this.itemCount = itemCount;
	}
}
