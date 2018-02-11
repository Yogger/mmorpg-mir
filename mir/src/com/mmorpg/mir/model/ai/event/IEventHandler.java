package com.mmorpg.mir.model.ai.event;

import com.mmorpg.mir.model.ai.AI;

public interface IEventHandler {

	public Event getEvent();

	public void handleEvent(Event event, AI ai);

}
