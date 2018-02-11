package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

public class SummonAi extends NpcAi {
	public SummonAi() {
		super();
		this.addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
		this.addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
		this.addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
		this.addEventHandler(EventHandlers.NOTSEEPLAYER_SUMMON_EH.getHandler());
		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());

		this.addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
		this.addStateHandler(StateHandlers.ACTIVE_SUMMON_SH.getHandler());
		this.addStateHandler(StateHandlers.NONE_MONSTER_SH.getHandler());
		this.addStateHandler(StateHandlers.THINKING_SUMMON_SH.getHandler());
	}
}