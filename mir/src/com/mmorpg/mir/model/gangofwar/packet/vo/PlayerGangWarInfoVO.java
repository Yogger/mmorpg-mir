package com.mmorpg.mir.model.gangofwar.packet.vo;

public class PlayerGangWarInfoVO {
	private int continueKill;
	private int totalKill;
	private int maxContinueKill;
	private int rank;
	private long lastBackHomeTime;

	public int getContinueKill() {
		return continueKill;
	}

	public void setContinueKill(int continueKill) {
		this.continueKill = continueKill;
	}

	public int getTotalKill() {
		return totalKill;
	}

	public void setTotalKill(int totalKill) {
		this.totalKill = totalKill;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getMaxContinueKill() {
		return maxContinueKill;
	}

	public void setMaxContinueKill(int maxContinueKill) {
		this.maxContinueKill = maxContinueKill;
	}

	public long getLastBackHomeTime() {
		return lastBackHomeTime;
	}

	public void setLastBackHomeTime(long lastBackHomeTime) {
		this.lastBackHomeTime = lastBackHomeTime;
	}

}
