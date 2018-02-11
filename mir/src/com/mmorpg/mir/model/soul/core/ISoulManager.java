package com.mmorpg.mir.model.soul.core;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.resource.SoulResource;

public interface ISoulManager {
	long getIntervalTime();

	public SoulResource getSoulResource(int level);

	public boolean isOpen(Player player);

}
