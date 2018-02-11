package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.AggressionDesire;
import com.mmorpg.mir.model.ai.desires.impl.AggressionWalkDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;

public class ActiveAggroStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.ACTIVE;
	}

	/**
	 * State ACTIVE AI AggressiveMonsterAi AI GuardAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		Npc owner = (Npc) ai.getOwner();

		// if there are players visible - add AggressionDesire filter
		int creatureCount = 0;
		for (VisibleObject visibleObject : owner.getKnownList()) {
			if (visibleObject instanceof Creature) {
				if (owner.isEnemy((Creature) visibleObject)) {
					creatureCount++;
					break;
				}
			}
		}

		if (creatureCount > 0) {
			if (owner.hasWalkRoutes() && owner.canMove()) {
				ai.addDesire(new AggressionWalkDesire(owner, AIState.ACTIVE.getPriority()));
			} else {
				ai.addDesire(new AggressionDesire(owner, AIState.ACTIVE.getPriority()));
			}
		}

		if (ai.desireQueueSize() == 0)
			ai.handleEvent(Event.NOTHING_TODO);
		else
			ai.schedule();
	}
}
