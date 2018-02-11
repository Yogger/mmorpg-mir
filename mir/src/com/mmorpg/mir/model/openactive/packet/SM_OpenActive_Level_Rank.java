package com.mmorpg.mir.model.openactive.packet;

public class SM_OpenActive_Level_Rank {

	private int levelRank;
	
	public static SM_OpenActive_Level_Rank valueOf(int levelRank) {
		SM_OpenActive_Level_Rank sm = new SM_OpenActive_Level_Rank();
		sm.levelRank = levelRank;
		return sm;
	}

	public int getLevelRank() {
		return levelRank;
	}

	public void setLevelRank(int levelRank) {
		this.levelRank = levelRank;
	}

}
