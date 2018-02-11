package com.mmorpg.mir.model.warship.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface WarshipService {

	/**
	 * 获取膜拜信息
	 * 
	 * @param player
	 */
	void getWarshipStatus(Player player);

	/**
	 * 膜拜
	 * 
	 * @param player
	 * @param isSupport
	 */
	void warShipKing(Player player, boolean isSupport);

	/**
	 * 刷新奖励
	 * 
	 * @param player
	 * @param isGold
	 */
	void warShipRefreshReward(Player player, boolean isGold);
}
