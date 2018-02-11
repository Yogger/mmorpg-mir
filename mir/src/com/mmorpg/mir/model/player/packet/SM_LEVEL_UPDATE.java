package com.mmorpg.mir.model.player.packet;

public class SM_LEVEL_UPDATE {
	private long playerId;
	private int level;
	private long currentHp;
	private long currentMp;

	public static SM_LEVEL_UPDATE valueOf(long playerId, int level, long currentHp, long currentMp) {
		SM_LEVEL_UPDATE plu = new SM_LEVEL_UPDATE();
		plu.playerId = playerId;
		plu.level = level;
		plu.currentHp = currentHp;
		plu.currentMp = currentMp;
		return plu;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public long getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(long currentMp) {
		this.currentMp = currentMp;
	}

}
