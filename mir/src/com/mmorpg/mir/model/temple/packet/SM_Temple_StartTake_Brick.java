package com.mmorpg.mir.model.temple.packet;

public class SM_Temple_StartTake_Brick {
	private int country;
	private long endTime;
	private int code;

	public static SM_Temple_StartTake_Brick valueOf(int country, long endTime) {
		SM_Temple_StartTake_Brick sm = new SM_Temple_StartTake_Brick();
		sm.country = country;
		sm.endTime = endTime;
		return sm;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
