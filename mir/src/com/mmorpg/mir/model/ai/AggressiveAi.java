package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.state.StateHandlers;

public class AggressiveAi extends MonsterAi {
	public AggressiveAi() {
		super();

		/**
		 * State handlers
		 */
		this.addStateHandler(StateHandlers.ACTIVE_AGGRO_SH.getHandler());
		this.addStateHandler(StateHandlers.ACTIVE_THINKING_SH.getHandler());
	}
}
