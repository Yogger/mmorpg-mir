package com.mmorpg.mir.model.commonactivity.model;

public class ServerGoldTreasuryLog {
	private String playerName;

	private String itemId;
	
	private int groupId;
	
	private long time;

	public static ServerGoldTreasuryLog valueOf(String playerName, String itemId, int groupId) {
		ServerGoldTreasuryLog log = new ServerGoldTreasuryLog();
		log.playerName = playerName;
		log.itemId = itemId;
		log.groupId = groupId;
		log.setTime(System.currentTimeMillis());
		return log;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
