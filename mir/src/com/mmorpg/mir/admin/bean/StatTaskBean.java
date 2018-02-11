package com.mmorpg.mir.admin.bean;

public class StatTaskBean {
	private String key;
	private long packetTimes;
	private long totalTimes;
	private long overTimes;

	public long getPacketTimes() {
		return packetTimes;
	}

	public void setPacketTimes(long packetTimes) {
		this.packetTimes = packetTimes;
	}

	public long getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(long totalTimes) {
		this.totalTimes = totalTimes;
	}

	public long getOverTimes() {
		return overTimes;
	}

	public void setOverTimes(long overTimes) {
		this.overTimes = overTimes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
