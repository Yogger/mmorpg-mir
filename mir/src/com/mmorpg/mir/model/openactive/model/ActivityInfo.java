package com.mmorpg.mir.model.openactive.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ActivityInfo {
	private long beginTime;
	private long endTime;

	public static ActivityInfo valueOf(long beginTime, long endTime) {
		ActivityInfo result = new ActivityInfo();
		result.beginTime = beginTime;
		result.endTime = endTime;
		return result;
	}

	@JsonIgnore
	public boolean isOpenning() {
		return System.currentTimeMillis() <= endTime && System.currentTimeMillis() >= beginTime;
	}

	@JsonIgnore
	public void reset(long beginTime, long endTime) {
		this.beginTime = beginTime;
		this.endTime = endTime;
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
