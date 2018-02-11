package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface OfflineExpService {

	/**
	 * 打开离线经验面板
	 * 
	 * @param player
	 */
	void openOfflineExp(Player player);

	/**
	 * 领取离线奖励
	 * 
	 * @param player
	 * @param type
	 */
	void offlineExpReward(Player player, int type);
}
