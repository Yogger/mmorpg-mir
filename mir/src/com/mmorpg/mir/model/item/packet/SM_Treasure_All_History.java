package com.mmorpg.mir.model.item.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.item.model.PlayerTreasureInfo;

public class SM_Treasure_All_History {

	private ArrayList<PlayerTreasureInfo> playerTreasureHistory;

	public static SM_Treasure_All_History valueOf(ArrayList<PlayerTreasureInfo> playerTreasureHistory) {
		SM_Treasure_All_History result = new SM_Treasure_All_History();
		result.playerTreasureHistory = playerTreasureHistory;
		return result;
	}

	public ArrayList<PlayerTreasureInfo> getPlayerTreasureHistory() {
		return playerTreasureHistory;
	}

	public void setPlayerTreasureHistory(ArrayList<PlayerTreasureInfo> playerTreasureHistory) {
		this.playerTreasureHistory = playerTreasureHistory;
	}

}
