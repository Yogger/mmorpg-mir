package com.mmorpg.mir.model.boss.model;

import java.util.Map;

import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class BossView {
	private String id;
	private int level;
	private int status;
	private long lastRefreshTime;
	private String spawnId;
	private Map<Long, PlayerSimpleInfo> lastByKillPlayers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public String getSpawnId() {
		return spawnId;
	}

	public void setSpawnId(String spawnId) {
		this.spawnId = spawnId;
	}

	public Map<Long, PlayerSimpleInfo> getLastByKillPlayers() {
		return lastByKillPlayers;
	}

	public void setLastByKillPlayers(Map<Long, PlayerSimpleInfo> lastByKillPlayers) {
		this.lastByKillPlayers = lastByKillPlayers;
	}

}
