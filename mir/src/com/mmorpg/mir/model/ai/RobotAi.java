package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.EventHandlers;

public class RobotAi extends AggressiveAi {
	public RobotAi() {
		super();

		this.addEventHandler(EventHandlers.SPAWN_EH.getHandler());
	}
}
