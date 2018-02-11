package com.mmorpg.mir.model.country.packet;

public class SM_Query_Mobilization {

	private int leftCount;

	public static SM_Query_Mobilization valueOf(int leftCount) {
		SM_Query_Mobilization sm = new SM_Query_Mobilization();
		sm.leftCount = leftCount;
		return sm;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

}
