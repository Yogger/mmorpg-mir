package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;

public class CountryObjectAi extends NpcAi {
	public CountryObjectAi() {
		/**
		 * Event Handlers
		 */
		this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
		this.addEventHandler(EventHandlers.DIED_EH.getHandler());
		this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());
	}
}