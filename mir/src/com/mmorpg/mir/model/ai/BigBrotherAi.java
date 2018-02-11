package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;
import com.mmorpg.mir.model.ai.state.StateHandlers;

/**
 * 大B哥的AI类型
 * 
 * @author Kuang Hao
 * @since v1.0 2015-8-3
 * 
 */
public class BigBrotherAi extends NpcAi {
	public BigBrotherAi() {
		super();
		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());

		this.addStateHandler(StateHandlers.ACTIVE_BIGBROTHER_SUMMON_SH.getHandler());
		this.addStateHandler(StateHandlers.NONE_MONSTER_SH.getHandler());
	}
}