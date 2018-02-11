package com.mmorpg.mir.model.welfare.service;

import com.mmorpg.mir.model.gameobjects.Player;

public interface ClawbackService {

	/**
	 * 查看追回收益
	 * 
	 * @param player
	 */
	void openClawback(Player player);

	/**
	 * 追回收益
	 * 
	 * @param player
	 * @param eventId
	 * @param currencyType
	 */
	void clawback(Player player, int eventId, int currencyType);

	/**
	 * 一键追回
	 * 
	 * @param player
	 * @param clawbackType
	 */
	void autoClawback(Player player, int clawbackType);

	/**
	 * 至尊追回
	 * 
	 * @param player
	 * @param clawbackType
	 */
	void supermeClawback(Player player, int clawbackType);

	/**
	 * 获取可领奖次数
	 * 
	 * @param player
	 * @param code
	 */
	void getTagLight(Player player, int code);

	void refreshClawbackData(Player player);
}
