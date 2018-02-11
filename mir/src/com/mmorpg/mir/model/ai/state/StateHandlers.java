package com.mmorpg.mir.model.ai.state;

import com.mmorpg.mir.model.ai.state.handler.ActiveAggroStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ActiveBigBrotherSummonStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ActiveNpcStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ActiveRouteStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ActiveSummonStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ActiveThinkingStateHandler;
import com.mmorpg.mir.model.ai.state.handler.AttackingStateHandler;
import com.mmorpg.mir.model.ai.state.handler.DeleteStateHandler;
import com.mmorpg.mir.model.ai.state.handler.MovingToHomeStateHandler;
import com.mmorpg.mir.model.ai.state.handler.NoneNpcStateHandler;
import com.mmorpg.mir.model.ai.state.handler.RestingStateHandler;
import com.mmorpg.mir.model.ai.state.handler.RouteActiveAggroStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ThinkingRouteStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ThinkingStateHandler;
import com.mmorpg.mir.model.ai.state.handler.ThinkingSummonStateHandler;

public enum StateHandlers {
	/**
	 * AIState.MOVINGTOHOME
	 */
	MOVINGTOHOME_SH(new MovingToHomeStateHandler()),
	/**
	 * AIState.NONE
	 */
	NONE_MONSTER_SH(new NoneNpcStateHandler()),
	/**
	 * AIState.ATTACKING
	 */
	ATTACKING_SH(new AttackingStateHandler()),
	/**
	 * AIState.THINKING
	 */
	THINKING_SH(new ThinkingStateHandler()),
	/**
	 * AIState.THINKING
	 */
	ACTIVE_THINKING_SH(new ActiveThinkingStateHandler()),
	/**
	 * AIState.THINKING_SUMMON_SH
	 */
	THINKING_SUMMON_SH(new ThinkingSummonStateHandler()),
	/**
	 * AIState.THINKING_LORRY_SH
	 */
	THINKING_ROUTE_SH(new ThinkingRouteStateHandler()),
	/**
	 * AIState.ACTIVE_NPC_SH
	 */
	ACTIVE_NPC_SH(new ActiveNpcStateHandler()),
	/**
	 * AIState.ACTIVE_AGGRO_SH
	 */
	ACTIVE_AGGRO_SH(new ActiveAggroStateHandler()),
	/**
	 * AIState.ROUTE_ACTIVE_AGGRO_SH
	 */
	ROUTE_ACTIVE_AGGRO_SH(new RouteActiveAggroStateHandler()),
	/**
	 * AIState.ACTIVE_ROUTE_SH
	 */
	ACTIVE_ROUTE_SH(new ActiveRouteStateHandler()),
	/**
	 * AIState.ACTIVE_SUMMON_SH
	 */
	ACTIVE_SUMMON_SH(new ActiveSummonStateHandler()),
	/**
	 * AIState.ACTIVE_SUMMON_SH
	 */
	ACTIVE_BIGBROTHER_SUMMON_SH(new ActiveBigBrotherSummonStateHandler()),

	/**
	 * AIState.RESTING
	 */
	RESTING_SH(new RestingStateHandler()),
	/**
	 * AIState.DELETE
	 */
	DELETE_SH(new DeleteStateHandler());

	private IStateHandler stateHandler;

	private StateHandlers(IStateHandler stateHandler) {
		this.stateHandler = stateHandler;
	}

	public IStateHandler getHandler() {
		return stateHandler;
	}
}
