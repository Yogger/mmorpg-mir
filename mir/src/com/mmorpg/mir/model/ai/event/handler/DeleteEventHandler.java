package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;

public class DeleteEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.DELETE;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		ai.setAiState(AIState.DELETE);
	}
}
