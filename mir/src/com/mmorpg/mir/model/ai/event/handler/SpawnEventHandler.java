package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;

public class SpawnEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.SPAWN;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		ai.setAiState(AIState.ACTIVE);
		ai.schedule();
		ai.getOwner().getKnownList().doUpdate(false);
	}
}
