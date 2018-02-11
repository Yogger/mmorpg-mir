package com.mmorpg.mir.model.player.packet;

public class SM_Gray_Left {

	private int grayLeftTime;

	public static SM_Gray_Left valueOf(int leftTime) {
		SM_Gray_Left sm = new SM_Gray_Left();
		sm.grayLeftTime = leftTime;
		return sm;
	}
	public int getGrayLeftTime() {
		return grayLeftTime;
	}

	public void setGrayLeftTime(int grayLeftTime) {
		this.grayLeftTime = grayLeftTime;
	}
	
}
