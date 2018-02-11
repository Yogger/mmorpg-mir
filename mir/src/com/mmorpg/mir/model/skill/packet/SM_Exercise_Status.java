package com.mmorpg.mir.model.skill.packet;

public class SM_Exercise_Status {

	private int avaliable;
	
	public static SM_Exercise_Status valueOf(int count) {
		SM_Exercise_Status sm = new SM_Exercise_Status();
		sm.avaliable = count;
		return sm;
	}

	public int getAvaliable() {
		return avaliable;
	}

	public void setAvaliable(int avaliable) {
		this.avaliable = avaliable;
	}
	
}
