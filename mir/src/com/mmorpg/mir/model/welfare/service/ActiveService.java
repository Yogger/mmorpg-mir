package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface ActiveService {

	/**
	 * 打开活跃值信息
	 * 
	 * @param player
	 */
	void openActiveValue(Player player);

	/**
	 * 领取激活奖励
	 * 
	 * @param player
	 * @param value
	 *            活跃值
	 */
	void activeValueReward(Player player, int value);
}
