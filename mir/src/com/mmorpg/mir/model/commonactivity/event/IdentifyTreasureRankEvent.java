package com.mmorpg.mir.model.commonactivity.event;

import com.windforce.common.event.event.IEvent;

public class IdentifyTreasureRankEvent implements IEvent {
	private long playerId;
	
	private String activeName;
	
	private String playerName;
	
	private String rewardId;
	
	private boolean isBigTreasure;

	public static IdentifyTreasureRankEvent valueOf(long playerId, String activeName, String playerName, String rewardId, boolean isBigTreasure){
		IdentifyTreasureRankEvent event = new IdentifyTreasureRankEvent();
		event.playerId = playerId;
		event.activeName = activeName;
		event.playerName = playerName;
		event.rewardId = rewardId;
		event.isBigTreasure = isBigTreasure;
		return event;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public String getRewardId() {
		return rewardId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public boolean isBigTreasure() {
		return isBigTreasure;
	}

	public void setBigTreasure(boolean isBigTreasure) {
		this.isBigTreasure = isBigTreasure;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
}
