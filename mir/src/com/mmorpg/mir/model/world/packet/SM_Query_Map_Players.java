package com.mmorpg.mir.model.world.packet;

public class SM_Query_Map_Players {

	private Integer[] playerCounts;
	
	public static SM_Query_Map_Players valueOf(Integer[] info) {
		SM_Query_Map_Players sm = new SM_Query_Map_Players();
		sm.playerCounts = info;
		return sm;
	}

	public Integer[] getPlayerCounts() {
		return playerCounts;
	}

	public void setPlayerCounts(Integer[] playerCounts) {
		this.playerCounts = playerCounts;
	}
}