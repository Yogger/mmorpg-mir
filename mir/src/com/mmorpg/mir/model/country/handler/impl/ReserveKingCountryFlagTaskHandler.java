package com.mmorpg.mir.model.country.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.country.event.ReserveKingFinishFlagEvent;
import com.mmorpg.mir.model.country.handler.AbstractReserveKingTaskHandler;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class ReserveKingCountryFlagTaskHandler extends AbstractReserveKingTaskHandler {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public ReserveTaskEnum getTaskType() {
		return ReserveTaskEnum.KILL_FLAG;
	}

	@ReceiverAnno
	public void receiveEvent(ReserveKingFinishFlagEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		handle(player);
	}

}
