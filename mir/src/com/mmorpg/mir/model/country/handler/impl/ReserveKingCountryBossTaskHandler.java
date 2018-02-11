package com.mmorpg.mir.model.country.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.country.handler.AbstractReserveKingTaskHandler;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class ReserveKingCountryBossTaskHandler extends AbstractReserveKingTaskHandler {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public ReserveTaskEnum getTaskType() {
		return ReserveTaskEnum.COUNTRY_BOSS;
	}

	@ReceiverAnno
	public void bossDie(BossDieEvent event) {
		BossResource resource = BossManager.getInstance().getBossResource(event.getBossId(), true);
		Player player = playerManager.getPlayer(event.getOwner());
		if (resource.getGroup() == player.getCountryValue()) {
			handle(player);
		}
	}
}
