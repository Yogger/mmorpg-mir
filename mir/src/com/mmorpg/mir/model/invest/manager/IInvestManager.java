package com.mmorpg.mir.model.invest.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.model.InvestType;
import com.mmorpg.mir.model.invest.packet.SM_HistoryInfo;

public interface IInvestManager {

	/**
	 * 购买投资
	 * 
	 * @param player
	 * @param type
	 *            投资类型
	 */
	public void buy(Player player, InvestType type);

	/**
	 * 领取投资奖励
	 * 
	 * @param player
	 * @param type
	 *            投资类型
	 * @param dayIndex
	 *            第几天的奖励
	 */
	public void drawReward(Player player, InvestType type, String resourceId);

	/**
	 * 获取全服历史信息
	 * 
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public SM_HistoryInfo getHistoryRecord();
}
