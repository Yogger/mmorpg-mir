package com.mmorpg.mir.model.operator.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class BanPlayer {
	private long playerId;
	private String server;
	private String name;
	private String account;

	public static BanPlayer valueOf(Player player) {
		BanPlayer bp = new BanPlayer();
		bp.playerId = player.getObjectId();
		bp.name = player.getName();
		bp.account = player.getPlayerEnt().getAccountName();
		bp.server = player.getPlayerEnt().getServer();
		return bp;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
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
		BanPlayer other = (BanPlayer) obj;
		if (playerId != other.playerId)
			return false;
		return true;
	}

}
