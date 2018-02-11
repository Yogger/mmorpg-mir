package com.mmorpg.mir.model.country.packet;

public class SM_Country_Flag_Debuff_Endtime {

	private long startTime;

	public static SM_Country_Flag_Debuff_Endtime valueOf(long t) {
		SM_Country_Flag_Debuff_Endtime sm = new SM_Country_Flag_Debuff_Endtime();
		sm.startTime = t;
		return sm;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
