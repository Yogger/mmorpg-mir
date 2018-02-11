package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Treasure_Storage_Update {
	private Map<Integer, Object> packUpdate;

	public static SM_Treasure_Storage_Update valueOf(Player player) {
		SM_Treasure_Storage_Update result = new SM_Treasure_Storage_Update();
		result.packUpdate = player.getTreasureWareHouse().collectUpdate();
		return result;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

}
