package com.mmorpg.mir.model.ai.state;

public enum AIState {
	THINKING(5), ACTIVE(3), ATTACKING(2), RESTING(1), MOVINGTOHOME(1), NONE(0), DELETE(0);

	private int priority;

	private AIState(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}
