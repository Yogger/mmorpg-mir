package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.impl.MoveToHomeDesire;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;

/**
 * @author ATracer
 * 
 */
public class MovingToHomeStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.MOVINGTOHOME;
	}

	/**
	 * State MOVINGTOHOME AI MonsterAi AI GuardAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();
		Npc npc = (Npc) ai.getOwner();
		npc.setTarget(null);
		npc.getAggroList().clear();
		ai.addDesire(new MoveToHomeDesire(npc, AIState.MOVINGTOHOME.getPriority()));

		ai.schedule();
	}
}
