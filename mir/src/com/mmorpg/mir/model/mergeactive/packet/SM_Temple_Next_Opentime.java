package com.mmorpg.mir.model.mergeactive.packet;

public class SM_Temple_Next_Opentime {
	private long nextOpenTime;

	public long getNextOpenTime() {
		return nextOpenTime;
	}

	public void setNextOpenTime(long nextOpenTime) {
		this.nextOpenTime = nextOpenTime;
	}
	
	public static SM_Temple_Next_Opentime valueOf(long time){
		SM_Temple_Next_Opentime sm = new SM_Temple_Next_Opentime();
		sm.nextOpenTime = time;
		return sm;
	}
}