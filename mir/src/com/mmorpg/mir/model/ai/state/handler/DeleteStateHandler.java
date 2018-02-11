package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.world.World;

public class DeleteStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.DELETE;
	}

	/**
	 * State NONE
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		((Npc) ai.getOwner()).getAggroList().clear();
		((Npc) ai.getOwner()).getDamages().clear();
		ai.stop();
		ai.getOwner().getMoveController().stop();
		ai.getOwner().getEffectController().removeAllEffects();
		if (ai.getOwner().isSpawned())
			World.getInstance().despawn(ai.getOwner());
	}
}
