package com.mmorpg.mir.model.gangofwar.event;

import com.windforce.common.event.event.IEvent;

public class KingAbdicateEvent implements IEvent {

	private Long ownerId;

	public static KingAbdicateEvent valueOf(Long ownerId) {
		KingAbdicateEvent event = new KingAbdicateEvent();
		event.ownerId = ownerId;
		return event;
	}

	@Override
	public long getOwner() {
		return ownerId;
	}

}
