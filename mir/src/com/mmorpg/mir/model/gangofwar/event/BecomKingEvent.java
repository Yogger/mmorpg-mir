package com.mmorpg.mir.model.gangofwar.event;

import com.windforce.common.event.event.IEvent;

public class BecomKingEvent implements IEvent {

	private Long ownerId;

	public static BecomKingEvent valueOf(Long ownerId) {
		BecomKingEvent event = new BecomKingEvent();
		event.ownerId = ownerId;
		return event;
	}

	@Override
	public long getOwner() {
		return ownerId;
	}

}
