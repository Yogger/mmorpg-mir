package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface OnlineService {
	/**
	 * 打开在线奖励
	 * 
	 * @param player
	 */
	void openOnline(Player player);

	/**
	 * 领取在线奖励
	 * 
	 * @param player
	 * @param index
	 */
	void onlineReward(Player player, int[] index);
}
