package com.mmorpg.mir.model.openactive.packet;

public class SM_Query_Consume_Act {

	private int consumeGold;
	private int rank;
	
	public static SM_Query_Consume_Act valueOf(int gold, int rank) {
		SM_Query_Consume_Act sm = new SM_Query_Consume_Act();
		sm.consumeGold = gold;
		sm.rank = rank;
		return sm;
	}
	
	public int getConsumeGold() {
		return consumeGold;
	}
	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
