package com.mmorpg.mir.model.agateinvest.manager;

import com.mmorpg.mir.model.agateinvest.model.InvestAgateType;
import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_HistoryInfo;
import com.mmorpg.mir.model.gameobjects.Player;

public interface IInvestAgateManager {

	/**
	 * 购买投资
	 * 
	 * @param player
	 * @param type
	 *            投资类型
	 */
	public void buy(Player player, InvestAgateType type);

	/**
	 * 领取投资奖励
	 * 
	 * @param player
	 * @param type
	 *            投资类型
	 * @param dayIndex
	 *            第几天的奖励
	 */
	public void drawReward(Player player, InvestAgateType type, String resourceId);

	/**
	 * 获取全服历史信息
	 * 
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public SM_Agate_HistoryInfo getHistoryRecord();
}
