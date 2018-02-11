package com.mmorpg.mir.model.welfare.packet;

public class SM_Active_Num {

	private byte activeLightNum;
	
	public static SM_Active_Num valueOf(int count) {
		SM_Active_Num sm = new SM_Active_Num();
		sm.activeLightNum = (byte) count;
		return sm;
	}

	public byte getActiveLightNum() {
		return activeLightNum;
	}

	public void setActiveLightNum(byte activeLightNum) {
		this.activeLightNum = activeLightNum;
	}

}
