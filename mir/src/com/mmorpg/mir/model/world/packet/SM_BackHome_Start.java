package com.mmorpg.mir.model.world.packet;

public class SM_BackHome_Start {

	private long endTime;
	private int code;
	
	public static SM_BackHome_Start valueOf(long t) {
		SM_BackHome_Start s = new SM_BackHome_Start();
		s.endTime = t;
		return s;
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
