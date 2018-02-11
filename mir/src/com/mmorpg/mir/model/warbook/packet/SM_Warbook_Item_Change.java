package com.mmorpg.mir.model.warbook.packet;

import java.util.Map;

import com.mmorpg.mir.model.warbook.model.Warbook;

public class SM_Warbook_Item_Change {

	private Map<String, Integer> itemCount;

	public static SM_Warbook_Item_Change valueOf(Warbook warBook) {
		SM_Warbook_Item_Change result = new SM_Warbook_Item_Change();
		result.itemCount = warBook.getItemCount();
		return result;
	}

	public Map<String, Integer> getItemCount() {
		return itemCount;
	}

	public void setItemCount(Map<String, Integer> itemCount) {
		this.itemCount = itemCount;
	}

}
