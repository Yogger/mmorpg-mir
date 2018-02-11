package com.mmorpg.mir.model.country.handler;

import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.gameobjects.Player;

public abstract class AbstractReserveKingTaskHandler {
	/**
	 * 获取任务类型
	 * 
	 * @return
	 */
	public abstract ReserveTaskEnum getTaskType();

	public void handle(Player player) {
		ReserveKing reserveKing = player.getCountry().getReserveKing();
		boolean isReserveKing = reserveKing.isReserveKing(player.getObjectId());
		boolean isDeprected = reserveKing.isDeprected();
		if (!isDeprected && isReserveKing) {
			reserveKing.addTaskCount(getTaskType());
		}

	}

}
