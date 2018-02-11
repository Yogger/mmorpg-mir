package com.mmorpg.mir.model.group.packet;

public class SM_Member_Level_Change {

	private long playerId;
	private int newLevel;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public static SM_Member_Level_Change valueOf(long pid, int level) {
		SM_Member_Level_Change sm = new SM_Member_Level_Change();
		sm.playerId = pid;
		sm.newLevel = level;
		return sm;
	}
}
