package com.mmorpg.mir.admin.bean;

public class StatNetReceiveBean {
	private String key;
	private long packetTimes;
	private long packetLengths;
	private long totalTimes;
	private long overTimes;
	private long decodeTimes;

	public long getPacketTimes() {
		return packetTimes;
	}

	public void setPacketTimes(long packetTimes) {
		this.packetTimes = packetTimes;
	}

	public long getPacketLengths() {
		return packetLengths;
	}

	public void setPacketLengths(long packetLengths) {
		this.packetLengths = packetLengths;
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

	public long getDecodeTimes() {
		return decodeTimes;
	}

	public void setDecodeTimes(long decodeTimes) {
		this.decodeTimes = decodeTimes;
	}

}
