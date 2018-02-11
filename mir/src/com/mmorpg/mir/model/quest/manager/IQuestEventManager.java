package com.mmorpg.mir.model.quest.manager;

import com.windforce.common.event.event.IEvent;

public interface IQuestEventManager {
	public void init();

	public void doEventRefreshQuest(IEvent event);
}
