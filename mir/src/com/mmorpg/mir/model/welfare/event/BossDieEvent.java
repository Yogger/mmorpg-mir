package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

/**
 * boss死亡
 * 
 * @author 37wan
 * 
 */
public class BossDieEvent implements IEvent {

	private long playerId;
	private String bossId;
	private String spawnKey;
	private boolean knowPlayer;
	private int rank;

	public static BossDieEvent valueOf(long playerId, String bossId, String spawnKey, boolean knowPlayer, int rank) {
		BossDieEvent event = new BossDieEvent();
		event.playerId = playerId;
		event.rank = rank;
		event.bossId = bossId;
		event.spawnKey = spawnKey;
		event.knowPlayer = knowPlayer;
		return event;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getBossId() {
		return bossId;
	}

	public void setBossId(String bossId) {
		this.bossId = bossId;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public boolean isKnowPlayer() {
		return knowPlayer;
	}

	public void setKnowPlayer(boolean knowPlayer) {
		this.knowPlayer = knowPlayer;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
