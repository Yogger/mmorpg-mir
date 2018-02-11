package com.mmorpg.mir.admin.packet;

public class GM_Celebrate_Set_ActivityOpen {
	private int type;
	private long beginTime;
	private long endTime;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
