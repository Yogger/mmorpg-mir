package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;

public class MostHatedChangedEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.MOST_HATED_CHANGED;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		ai.setAiState(AIState.THINKING);
	}
}
