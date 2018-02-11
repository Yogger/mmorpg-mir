package com.mmorpg.mir.model.warship.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.warship.resource.WarshipResource;

public interface IWarshipManager {
	void spawnAll();

	WarshipResource getWarshipResource(int key);

	void logGain(Player player, Reward reward);

	boolean isExpUpIn();
}
