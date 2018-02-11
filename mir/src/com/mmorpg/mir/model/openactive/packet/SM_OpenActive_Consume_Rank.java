package com.mmorpg.mir.model.openactive.packet;

public class SM_OpenActive_Consume_Rank {

	private int consumeRank;
	
	public static SM_OpenActive_Consume_Rank valueOf(int rank) {
		SM_OpenActive_Consume_Rank sm = new SM_OpenActive_Consume_Rank();
		sm.consumeRank = rank;
		return sm;
	}

	public int getConsumeRank() {
		return consumeRank;
	}

	public void setConsumeRank(int consumeRank) {
		this.consumeRank = consumeRank;
	}
	
}
