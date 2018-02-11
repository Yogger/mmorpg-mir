package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface GiftRewardService {

	/**
	 * 领取礼物
	 * 
	 * @param player
	 * @param giftType
	 */
	void getOneOffGift(Player player, int giftType);

	/**
	 * 查询红包
	 * 
	 * @param player
	 * @param deadTime
	 * @param spawnKey
	 */
	void queryRedGift(Player player, long deadTime, String spawnKey);

	/**
	 * 领取红包
	 * 
	 * @param player
	 * @param deadTime
	 * @param spawnKey
	 */
	void recieveRedGift(Player player, long deadTime, String spawnKey);
}
