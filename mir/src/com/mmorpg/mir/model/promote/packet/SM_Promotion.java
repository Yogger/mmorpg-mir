package com.mmorpg.mir.model.promote.packet;

public class SM_Promotion {

	private int stage;

	public static SM_Promotion valueOf(int s) {
		SM_Promotion sm = new SM_Promotion();
		sm.stage = s;
		return sm;
	}
	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	
}
