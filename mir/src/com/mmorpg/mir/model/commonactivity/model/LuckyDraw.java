package com.mmorpg.mir.model.commonactivity.model;

import java.util.Date;

public class LuckyDraw {
	private long payCount;

	private int drawCount;

	private String concurrentRewardId;
	
	private long lastClearTime;

	public void dayReset() {
		this.lastClearTime = new Date().getTime();
		this.payCount = 0;
		this.drawCount = 0;
		this.concurrentRewardId = null;
	}

	public boolean canDraw(int preCount) {
		return (payCount - (drawCount + 1) * preCount) >= 0;
	}

	public void addOneDraw(String rewardId) {
		this.concurrentRewardId = rewardId;
	}

	public void clearOneDraw() {
		drawCount++;
		this.concurrentRewardId = null;
	}

	public void addPayCount(long count) {
		this.payCount += count;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public int getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}

	public String getConcurrentRewardId() {
		return concurrentRewardId;
	}

	public void setConcurrentRewardId(String concurrentRewardId) {
		this.concurrentRewardId = concurrentRewardId;
	}

	public long getLastClearTime() {
		return lastClearTime;
	}

	public void setLastClearTime(long lastClearTime) {
		this.lastClearTime = lastClearTime;
	}
}
