package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.FollowTargetDesire;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Summon;

public class ThinkingSummonStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.THINKING;
	}

	/**
	 * State THINKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();

		Summon owner = (Summon) ai.getOwner();

		if (owner.tooFarFromHome(owner.getX(), owner.getY()) && owner.canMove()) {
			owner.getAggroList().clear();
			ai.addDesire(new FollowTargetDesire(owner, AIState.ACTIVE.getPriority()));
		} else {

			if (owner.getAggroList().getMostHated() != null) {
				ai.setAiState(AIState.ATTACKING);
				return;
			}

			if (owner.findEnemy())
				return;
		}

		ai.setAiState(AIState.ACTIVE);
	}
}
