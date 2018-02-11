package com.mmorpg.mir.model.countrycopy.packet.vo;

public class CountryCopyVO {

	private long copyStartTime;
	
	private int leftCount;
	
	private long lastEnterTime;

	public long getCopyStartTime() {
		return copyStartTime;
	}

	public void setCopyStartTime(long copyStartTime) {
		this.copyStartTime = copyStartTime;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

	public long getLastEnterTime() {
		return lastEnterTime;
	}

	public void setLastEnterTime(long lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}

}
