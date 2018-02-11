package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class HeartbeatEvent implements IEvent {

	public static final String EVENT_NAME = "player:Heartbeat";

	public static IEvent valueOf(long playerId) {
		HeartbeatEvent event = new HeartbeatEvent(playerId);
		return event;
	}

	private long playerId;

	public HeartbeatEvent(long playerId) {
		this.playerId = playerId;
	}

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
	}

}