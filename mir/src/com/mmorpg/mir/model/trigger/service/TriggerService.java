package com.mmorpg.mir.model.trigger.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface TriggerService {
	/**
	 * 客户端触发
	 * 
	 * @param id
	 * @param player
	 */
	void clientTrigger(String id, Player player);
}
