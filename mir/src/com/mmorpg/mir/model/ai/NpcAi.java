package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

/**
 * @author ATracer
 * 
 */
public class NpcAi extends AI {

	public NpcAi() {
		/**
		 * Event Handlers
		 */
		this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
		this.addEventHandler(EventHandlers.DIED_EH.getHandler());
		this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.SEEPLAYER_EH.getHandler());
		this.addEventHandler(EventHandlers.NOTSEEPLAYER_EH.getHandler());
		this.addEventHandler(EventHandlers.ROUTEOVER_EH.getHandler());
		this.addEventHandler(EventHandlers.DELETE_EH.getHandler());

		/**
		 * State Handlers
		 */
		this.addStateHandler(StateHandlers.ACTIVE_NPC_SH.getHandler());
		this.addStateHandler(StateHandlers.DELETE_SH.getHandler());
	}

}
