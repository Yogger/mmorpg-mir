package com.mmorpg.mir.model.rank.packet;

public class SM_World_Military_Level {

	private int rank;

	public static SM_World_Military_Level valueOf(int militaryRank) {
		SM_World_Military_Level sm = new SM_World_Military_Level();
		sm.rank = militaryRank;
		return sm;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
