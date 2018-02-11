package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.FollowTargetDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Summon;

public class ActiveSummonStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.ACTIVE;
	}

	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		Summon owner = (Summon) ai.getOwner();

		if (owner.canMove()) {
			ai.addDesire(new FollowTargetDesire(owner, AIState.ACTIVE.getPriority()));
		}

		if (ai.desireQueueSize() == 0)
			ai.handleEvent(Event.NOTHING_TODO);
		else
			ai.schedule();
	}
}
