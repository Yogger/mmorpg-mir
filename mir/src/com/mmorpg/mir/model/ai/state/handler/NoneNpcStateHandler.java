package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;

public class NoneNpcStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.NONE;
	}

	/**
	 * State NONE
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		((Npc) ai.getOwner()).getAggroList().clear();
		ai.stop();
	}
}
