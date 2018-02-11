package com.mmorpg.mir.model.ai.state.handler;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.AttackDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;

public class AttackingStateHandler implements IStateHandler {

	private static final Logger logger = Logger.getLogger(AttackingStateHandler.class);

	@Override
	public AIState getState() {
		return AIState.ATTACKING;
	}

	/**
	 * State ATTACKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();

		Npc owner = (Npc) ai.getOwner();

		Creature target = owner.getAggroList().getMostHated();
		if (target == null) {
			ai.setAiState(AIState.THINKING);
			return;
		}

		owner.setTarget(target);

		if (!owner.getSkillSelector().getItems().isEmpty()) {
			ai.addDesire(new AttackDesire(owner, target, AIState.ATTACKING.getPriority()));
		} else {
			logger.error(String.format("npc id [%s] key [%s] name [%s] not any skill found Exception",
					owner.getObjectId(), owner.getObjectKey(), owner.getName()));
		}

		if (ai.desireQueueSize() == 0)
			ai.handleEvent(Event.NOTHING_TODO);
		else
			ai.schedule();
	}
}
