package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.WalkDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class ActiveNpcStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.ACTIVE;
	}

	/**
	 * State ACTIVE AI NpcAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		Npc owner = (Npc) ai.getOwner();
		// TODO 怪物晃一晃
		if (owner.getKnownList().isActive() && owner.hasWalkRoutes() && owner.canMove()) {
			ai.addDesire(new WalkDesire(owner, AIState.ACTIVE.getPriority()));
		}

		if (ai.desireQueueSize() == 0)
			ai.handleEvent(Event.NOTHING_TODO);
		else
			ai.schedule();
	}
}
