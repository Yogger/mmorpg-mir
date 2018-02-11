package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.RestoreHealthDesire;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;

public class RestingStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.RESTING;
	}

	/**
	 * State RESTING AI NpcAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.addDesire(new RestoreHealthDesire((Npc) ai.getOwner(), AIState.RESTING.getPriority()));
	}
}
