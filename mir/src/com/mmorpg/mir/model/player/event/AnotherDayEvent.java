package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class AnotherDayEvent implements IEvent {

	public static final String EVENT_NAME = "system:24time";

	public static IEvent valueOf(long playerId) {
		AnotherDayEvent event = new AnotherDayEvent(playerId);
		return event;
	}

	private long playerId;

	public AnotherDayEvent(long playerId) {
		this.playerId = playerId;
	}

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
	}

}