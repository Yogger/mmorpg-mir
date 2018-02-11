package com.mmorpg.mir.admin.bean;

public class StatEventBean {
	private String key;
	private long eventTimes;
	private long eventTotalTime;
	private long eventOverTimes;

	public long getEventTimes() {
		return eventTimes;
	}

	public void setEventTimes(long eventTimes) {
		this.eventTimes = eventTimes;
	}

	public long getEventTotalTime() {
		return eventTotalTime;
	}

	public void setEventTotalTime(long eventTotalTime) {
		this.eventTotalTime = eventTotalTime;
	}

	public long getEventOverTimes() {
		return eventOverTimes;
	}

	public void setEventOverTimes(long eventOverTimes) {
		this.eventOverTimes = eventOverTimes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
