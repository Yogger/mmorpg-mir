package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class LevelUpEvent implements IEvent {

	public static final String EVENT_NAME = "player:LevelUp";

	public static IEvent valueOf(long playerId, int level, String playerName) {
		LevelUpEvent event = new LevelUpEvent(playerId, level, playerName);
		return event;
	}

	private long playerId;
	private int level;
	private String playerName;

	public LevelUpEvent(long playerId, int level, String playerName) {
		this.playerId = playerId;
		this.level = level;
		this.playerName = playerName;
	}

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
	}

	public int getLevel() {
		return level;
	}

	public String getPlayerName() {
		return playerName;
	}

}