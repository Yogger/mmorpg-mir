package com.mmorpg.mir.model.operator.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class OpForbidChat {
	private long playerId;
	private long endTime;
	private String name;
	private String server;
	private String account;

	public static OpForbidChat valueOf(Player player, long endTime) {
		OpForbidChat ofb = new OpForbidChat();
		ofb.playerId = player.getObjectId();
		ofb.endTime = endTime;
		ofb.name = player.getName();
		ofb.server = player.getPlayerEnt().getServer();
		ofb.account = player.getPlayerEnt().getAccountName();
		return ofb;
	}

	@JsonIgnore
	public boolean end() {
		if (endTime != 0 && endTime <= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (playerId ^ (playerId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpForbidChat other = (OpForbidChat) obj;
		if (playerId != other.playerId)
			return false;
		return true;
	}

}
