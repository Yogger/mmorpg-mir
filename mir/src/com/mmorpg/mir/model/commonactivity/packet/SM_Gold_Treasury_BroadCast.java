package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Gold_Treasury_BroadCast {
	private String activeName;
	
	private String playerName;
	
	private int groupId;

	private String rewardId;

	public static SM_Gold_Treasury_BroadCast valueOf(String activeName, String playerName, int groupId, String rewardId) {
		SM_Gold_Treasury_BroadCast sm = new SM_Gold_Treasury_BroadCast();
		sm.activeName = activeName;
		sm.playerName = playerName;
		sm.groupId = groupId;
		sm.rewardId = rewardId;
		return sm;
	}
	
	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
