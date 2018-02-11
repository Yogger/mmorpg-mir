package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

public class MonsterAi extends NpcAi {
	public MonsterAi() {
		super();
		this.addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
		this.addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
		this.addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
		this.addEventHandler(EventHandlers.BACKHOME_EH.getHandler());
		this.addEventHandler(EventHandlers.FARFROMHOME_EH.getHandler());
		this.addEventHandler(EventHandlers.RESTOREDHEALTH_EH.getHandler());

		this.addStateHandler(StateHandlers.MOVINGTOHOME_SH.getHandler());
		this.addStateHandler(StateHandlers.NONE_MONSTER_SH.getHandler());
		this.addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
		this.addStateHandler(StateHandlers.THINKING_SH.getHandler());
		this.addStateHandler(StateHandlers.RESTING_SH.getHandler());
	}
}