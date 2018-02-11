package com.mmorpg.mir.model.country.packet;

public class SM_Country_ForbidChat {
	private long endTime;

	public static SM_Country_ForbidChat valueOf(long endTime) {
		SM_Country_ForbidChat sm = new SM_Country_ForbidChat();
		sm.endTime = endTime;
		return sm;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
