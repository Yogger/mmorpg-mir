package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;

public class ThinkingRouteStateHandler implements IStateHandler {
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

		ai.getOwner().getObserveController().notifyRouteOverObservers();

		ai.setAiState(AIState.NONE);
	}
}
