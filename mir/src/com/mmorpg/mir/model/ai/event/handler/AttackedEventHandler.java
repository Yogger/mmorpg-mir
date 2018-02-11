package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;

public class AttackedEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.ATTACKED;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		if (ai.getAiState() != AIState.MOVINGTOHOME) {
			ai.setAiState(AIState.ATTACKING);
			ai.schedule();
		}
	}
}
