package com.mmorpg.mir.admin.bean;

public class StatNetSendBean {
	private String key;
	private long packetTimes;
	private long packetLengths;
	private long encodeTimes;

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getEncodeTimes() {
		return encodeTimes;
	}

	public void setEncodeTimes(long encodeTimes) {
		this.encodeTimes = encodeTimes;
	}

}
