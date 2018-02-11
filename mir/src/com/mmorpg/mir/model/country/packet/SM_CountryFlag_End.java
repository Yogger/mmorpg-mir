package com.mmorpg.mir.model.country.packet;

public class SM_CountryFlag_End {

	private long nextStartTime;

	public static SM_CountryFlag_End valueOf(long t) {
		SM_CountryFlag_End sm = new SM_CountryFlag_End();
		sm.nextStartTime = t;
		return sm;
	}
	
	public long getNextStartTime() {
		return nextStartTime;
	}

	public void setNextStartTime(long nextStartTime) {
		this.nextStartTime = nextStartTime;
	}
	
}
