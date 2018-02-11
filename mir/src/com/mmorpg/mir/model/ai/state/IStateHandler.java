package com.mmorpg.mir.model.ai.state;

import com.mmorpg.mir.model.ai.AI;

public interface IStateHandler {

	public AIState getState();

	public void handleState(AIState state, AI ai);

}
