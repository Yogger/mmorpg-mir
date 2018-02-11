package com.mmorpg.mir.model.countrycopy.packet;

public class SM_CountryCopy_Start {

	private long startTime;
	
	private boolean enrolled;
	
	public static SM_CountryCopy_Start valueOf(long startTime, boolean enrolled) {
		SM_CountryCopy_Start sm = new SM_CountryCopy_Start();
		sm.enrolled = enrolled;
		sm.startTime = startTime;
		return sm;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isEnrolled() {
		return enrolled;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

}
