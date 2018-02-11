package com.mmorpg.mir.model.country.packet;

public class SM_Hidden_Mission_Info {
	private int leftCount;
	
	public static SM_Hidden_Mission_Info valueOf(int count) {
		SM_Hidden_Mission_Info sm = new SM_Hidden_Mission_Info();
		sm.leftCount = count;
		return sm;
	}

	public int getLeftCount() {
    	return leftCount;
    }

	public void setLeftCount(int leftCount) {
    	this.leftCount = leftCount;
    }
	
}
