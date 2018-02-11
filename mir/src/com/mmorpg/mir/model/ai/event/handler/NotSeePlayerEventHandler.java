package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;

public class NotSeePlayerEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.NOT_SEE_PLAYER;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		int playerCount = 0;
		for (VisibleObject visibleObject : ai.getOwner().getKnownList()) {
			if (visibleObject instanceof Player)
				playerCount++;
		}
		if (playerCount == 0) {
			ai.getOwner().getKnownList().clear();
			ai.setAiState(AIState.THINKING);
		}
	}
}
