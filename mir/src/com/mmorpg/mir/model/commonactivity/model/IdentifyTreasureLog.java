package com.mmorpg.mir.model.commonactivity.model;

public class IdentifyTreasureLog {
	private String playerName;
	
	private String itemId;
	
	private boolean isBigTreasure;
	
	private long time;

	public static IdentifyTreasureLog valueOf(String playerName, String itermId, boolean isBigTreasure){
		IdentifyTreasureLog log = new IdentifyTreasureLog();
		log.playerName = playerName;
		log.itemId = itermId;
		log.isBigTreasure = isBigTreasure;
		log.setTime(System.currentTimeMillis());
		return log;
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

	public boolean isBigTreasure() {
		return isBigTreasure;
	}

	public void setBigTreasure(boolean isBigTreasure) {
		this.isBigTreasure = isBigTreasure;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
