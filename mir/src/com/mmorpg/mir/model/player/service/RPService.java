package com.mmorpg.mir.model.player.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface RPService {
	/**
	 * 添加人品
	 * 
	 * @param player
	 * @param killed
	 */
	void killAddRp(Player player, Player killed);
}
