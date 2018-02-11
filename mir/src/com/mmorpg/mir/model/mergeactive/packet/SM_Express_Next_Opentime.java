package com.mmorpg.mir.model.mergeactive.packet;

public class SM_Express_Next_Opentime {
	private long nextOpenTime;

	public long getNextOpenTime() {
		return nextOpenTime;
	}

	public void setNextOpenTime(long nextOpenTime) {
		this.nextOpenTime = nextOpenTime;
	}
	
	public static SM_Express_Next_Opentime valueOf(long time){
		SM_Express_Next_Opentime sm = new SM_Express_Next_Opentime();
		sm.nextOpenTime = time;
		return sm;
	}
	
}