package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface SignService {

	/**
	 * 打开签到面板
	 * 
	 * @param player
	 */
	void openSign(Player player);

	/**
	 * 签到当天的
	 * 
	 * @param player
	 */
	void signing(Player player);

	/**
	 * 补签
	 * 
	 * @param player
	 * @param reqTimeSeconds
	 */
	void fillSign(Player player, int reqTimeSeconds);

	/**
	 * 领取签到奖励
	 * 
	 * @param player
	 * @param days
	 */
	void signReward(Player player, int days);
}
