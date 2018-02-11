package com.mmorpg.mir.model.soul.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Soul_EnhanceCount_Change {
	
	private HashMap<String, Integer> enhanceItemCount;

	public static SM_Soul_EnhanceCount_Change valueOf(Player player) {
		SM_Soul_EnhanceCount_Change result = new SM_Soul_EnhanceCount_Change();
		result.enhanceItemCount = new HashMap<String, Integer>(player.getSoul().getEnhanceItemCount());
		return result;
	}

	public HashMap<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(HashMap<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}
	
}
