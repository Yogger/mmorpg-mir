package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.gameobjects.Npc;

public final class AggressionDesire extends AbstractDesire {
	protected Npc npc;

	public AggressionDesire(Npc npc, int desirePower) {
		super(desirePower);
		this.npc = npc;
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (npc == null)
			return false;

		if (npc.findEnemy())
			return true;

		npc.getAi().setAiState(AIState.THINKING);

		return true;
	}

	@Override
	public int getExecutionInterval() {
		return 1;
	}

	@Override
	public void onClear() {

	}
}
