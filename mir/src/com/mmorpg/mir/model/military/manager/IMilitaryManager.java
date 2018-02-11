package com.mmorpg.mir.model.military.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.resource.MilitaryLevelResource;
import com.mmorpg.mir.model.military.resource.MilitaryStarResource;
import com.mmorpg.mir.model.military.resource.MilitaryStrategyResource;

public interface IMilitaryManager {
	public MilitaryLevelResource getResource(int r);

	public MilitaryLevelResource getResource(int r, boolean isThrowException);

	public MilitaryStrategyResource getStrategyResource(int r);

	public int getInitalId(int section);

	public String getCurrentStar(Player player, String oldStarId);

	public String getCurrentStar(Player player);

	public MilitaryStarResource getMilitaryStarResource(String key, boolean throwException);
}
