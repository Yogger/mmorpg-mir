package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class OntheHourEvent implements IEvent {

	public static final String EVENT_NAME = "system:onTheHour";

	public static IEvent valueOf(long playerId) {
		OntheHourEvent event = new OntheHourEvent(playerId);
		return event;
	}

	private long playerId;

	public OntheHourEvent(long playerId) {
		this.playerId = playerId;
	}

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
	}

}