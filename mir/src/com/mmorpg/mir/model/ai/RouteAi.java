package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

public class RouteAi extends AI {

	public RouteAi() {
		/**
		 * Event Handlers
		 */
		this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
		this.addEventHandler(EventHandlers.DIED_EH.getHandler());
		this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.ROUTEOVER_EH.getHandler());
		this.addEventHandler(EventHandlers.SEEPLAYER_EH.getHandler());

		/**
		 * State Handlers
		 */
		this.addStateHandler(StateHandlers.ACTIVE_ROUTE_SH.getHandler());
		this.addStateHandler(StateHandlers.NONE_MONSTER_SH.getHandler());
		this.addStateHandler(StateHandlers.THINKING_ROUTE_SH.getHandler());
	}

}
