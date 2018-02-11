package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

public class RouteAggressiveAi extends AggressiveAi {
	public RouteAggressiveAi() {
		super();

		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());

		this.addStateHandler(StateHandlers.ROUTE_ACTIVE_AGGRO_SH.getHandler());
	}
}
