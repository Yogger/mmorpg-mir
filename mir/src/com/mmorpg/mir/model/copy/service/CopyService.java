package com.mmorpg.mir.model.copy.service;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public interface CopyService {

	/**
	 * 重置爬塔副本
	 * 
	 * @param player
	 */
	void ladderReset(Player player, int sign);

	/**
	 * 爬塔副本
	 * 
	 * @param player
	 * @param id
	 */
	void ladderReward(Player player, String id);

	/**
	 * 扫荡爬塔副本
	 * 
	 * @param player
	 */
	void batchLadder(Player player);

	/**
	 * 进入副本
	 * 
	 * @param id
	 * @param player
	 * @param trigger
	 */
	void enterCopy(final String id, final Player player, boolean trigger);

	/**
	 * 清理副本怪物
	 * 
	 * @param player
	 */
	void clearExpCopyMonster(Player player);

	/**
	 * 离开副本
	 * 
	 * @param id
	 * @param player
	 */
	void leaveCopy(Player player);

	/**
	 * 奖励
	 * 
	 * @param id
	 * @param player
	 */
	void reward(String id, Player player);

	/**
	 * 鼓舞
	 * 
	 * @param player
	 * @param gold
	 */
	void encourge(Player player, boolean gold);

	/**
	 * 购买次数
	 * 
	 * @param player
	 * @param id
	 */
	void buyCount(Player player, String id);

	/**
	 * boss奖励
	 * 
	 * @param player
	 * @param id
	 */
	void bossCopyReward(Player player, String id);

	void individualBossReset(Player player, String id);

	public Reward mutiLadderReward(Player player, String copyId);

	void mingJiangBossReset(Player player, String id);

	public void horseCopyReset(Player player, String id);

	/** 购买副本双倍奖励 */
	public void warBookReward(Player player, String id);

	public void showReward(Player player, String id);

	public void clearHorseEquipMonster(Player player);

	public void warbookReset(Player player, String id, boolean doubled);
}
