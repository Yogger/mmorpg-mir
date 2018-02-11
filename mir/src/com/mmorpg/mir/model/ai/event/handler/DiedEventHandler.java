package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;

public class DiedEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.DIED;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		ai.setAiState(AIState.NONE);
	}
}
