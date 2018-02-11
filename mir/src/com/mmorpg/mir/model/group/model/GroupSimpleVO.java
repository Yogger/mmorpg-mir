package com.mmorpg.mir.model.group.model;

public class GroupSimpleVO {
	private long leaderId;
	private String leaderName;
	private int size;
	private int leaderLevel;
	private int maxLevel;
	private int avgLevel;

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getLeaderLevel() {
		return leaderLevel;
	}

	public void setLeaderLevel(int leaderLevel) {
		this.leaderLevel = leaderLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getAvgLevel() {
		return avgLevel;
	}

	public void setAvgLevel(int avgLevel) {
		this.avgLevel = avgLevel;
	}

	public long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(long leaderId) {
		this.leaderId = leaderId;
	}

}
