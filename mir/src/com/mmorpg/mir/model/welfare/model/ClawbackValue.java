package com.mmorpg.mir.model.welfare.model;

public class ClawbackValue {

	private int currentNum;// 当前次数
	private int currentRuns;// 当前环数
	private long lastTime;// 最后访问的时间
	private long openTime;// 开启的时间
	
	private int currentClawbackNum; //当前找回使用的次数

	@Override
	public String toString() {
		return "currentNum = " + currentNum + " , currentRuns = " + currentRuns + ", lastTime = " + lastTime
				+ " , openTime = " + openTime;
	}

	public void clear() {
		currentNum = 0;
		currentRuns = 0;
		lastTime = System.currentTimeMillis();
		currentClawbackNum = 0;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public int getCurrentRuns() {
		return currentRuns;
	}

	public void setCurrentRuns(int currentRuns) {
		this.currentRuns = currentRuns;
	}

	public long getLastTime() {
		if (lastTime == 0) {
			lastTime = System.currentTimeMillis();
		}
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}
	public int getCurrentClawbackNum() {
		return currentClawbackNum;
	}

	public void setCurrentClawbackNum(int currentClawbackNum) {
		this.currentClawbackNum = currentClawbackNum;
	}
	
	public void logUseClawbackNum(int count) {
		this.currentClawbackNum += count;
	}

}