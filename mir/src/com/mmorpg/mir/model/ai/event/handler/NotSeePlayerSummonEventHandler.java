package com.mmorpg.mir.model.ai.event.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;

public class NotSeePlayerSummonEventHandler implements IEventHandler {
	@Override
	public Event getEvent() {
		return Event.NOT_SEE_PLAYER;
	}

	@Override
	public void handleEvent(Event event, AI ai) {
		boolean lost = true;
		Summon summon = (Summon) ai.getOwner();
		for (VisibleObject visibleObject : ai.getOwner().getKnownList()) {
			if (visibleObject instanceof Player) {
				if (summon.getMaster().equals(visibleObject)) {
					lost = false;
				}
			}
		}
		if (lost) {
			ai.getOwner().getKnownList().clear();
			ai.setAiState(AIState.ACTIVE);
		}
	}
}
