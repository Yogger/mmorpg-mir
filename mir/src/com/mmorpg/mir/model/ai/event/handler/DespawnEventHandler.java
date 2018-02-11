package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;

public class DespawnEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.DESPAWN;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		ai.handleEvent(Event.NOTHING_TODO);
	}
}
