package com.mmorpg.mir.model.kingofwar.packet.vo;

public class PlayerWarInfoVO {
	private int points;
	private int continueKill;
	private int totalKill;
	private int rank;
	private short bombValue;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

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

	public short getBombValue() {
		return bombValue;
	}

	public void setBombValue(short bombValue) {
		this.bombValue = bombValue;
	}

}
