package com.mmorpg.mir.model.respawn;

import com.mmorpg.mir.model.gameobjects.VisibleObject;

public interface ReliveService {
	void scheduleDecayAndReliveTask(final VisibleObject visibleObject);

	void scheduleDecayTask(final VisibleObject npc);
}
