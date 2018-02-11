package com.mmorpg.mir.model.contact.packet;

public class SM_FANS_MESSAGE {

	private int fans;

	public static SM_FANS_MESSAGE valueOf(int nums) {
		SM_FANS_MESSAGE sm = new SM_FANS_MESSAGE();
		sm.fans = nums;
		return sm;
	}
	
	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}
	
}
