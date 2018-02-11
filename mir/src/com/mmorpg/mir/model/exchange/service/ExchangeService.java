package com.mmorpg.mir.model.exchange.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface ExchangeService {

	/**
	 * 交易请求
	 * 
	 * @param player
	 * @param objId
	 */
	void exchangeRequest(final Player player, long objId);

	/**
	 * 添加交易金钱
	 * 
	 * @param player
	 * @param currencyType
	 * @param amount
	 */
	void exchangeAddCurrency(Player player, int currencyType, int amount);

	/**
	 * 添加交易物品
	 * 
	 * @param player
	 * @param index
	 */
	void exchangeAddItem(Player player, int index);

	/**
	 * 交易锁定
	 * 
	 * @param player
	 * @param lock
	 */
	void exchangeLock(Player player, boolean lock);

	/**
	 * 交易取消
	 * 
	 * @param player
	 */
	void exchangeCancel(Player player);

	/**
	 * 交易确认
	 * 
	 * @param player
	 */
	void exchangeConfirm(Player player);

	/**
	 * 删除交易道具
	 * 
	 * @param player
	 * @param exIndex
	 */
	void exchangeRemoveItem(Player player, int exIndex);

	/**
	 * 交换交易道具
	 * 
	 * @param player
	 * @param packIndex
	 * @param exIndex
	 */
	void exchangeExItem(Player player, int packIndex, int exIndex);
}
