package com.mmorpg.mir.model.player.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerChatSimple {
	private long playerId;
	private String name;
	private int level;
	private String server;

	public static PlayerChatSimple valueOf(Player player) {
		PlayerChatSimple pcs = new PlayerChatSimple();
		pcs.playerId = player.getObjectId();
		pcs.name = player.getName();
		pcs.level = player.getLevel();
		pcs.server = player.getPlayerEnt().getServer();
		return pcs;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

}
