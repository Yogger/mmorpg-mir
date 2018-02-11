package com.mmorpg.mir.model.country.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.country.service.HiddenMissionService;
import com.mmorpg.mir.model.express.event.ExpressBeenRobEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class HiddenMissionFacade {

	@Autowired
	private PlayerManager playerManager;
	
	@Autowired
	private HiddenMissionService hiddenMissionService;
	
	@ReceiverAnno
	public void killEvent(KillPlayerEvent event) {
		if (!event.isKillOtherCountryPlayer()) {
			return;
		}
		Player killed = playerManager.getPlayer(event.getKilledPlayerId());
		Player killer = playerManager.getPlayer(event.getOwner());
		
		// hiddenMissionService.protectCountryFlag(killer, killed);  守护国旗 换一种方式了
		hiddenMissionService.protectDiplomacy(killer, killed);
		hiddenMissionService.robBrickMan(killer, killed);
	}
	
	@ReceiverAnno
	public void robLorryEvent(ExpressBeenRobEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.isInGroup()) {
			hiddenMissionService.robLorryByGroup(player);
		} else {
			hiddenMissionService.robLorryByIndividual(player);
		}
	}
	
	@ReceiverAnno
	public void anotherdayRefresh(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getPlayerCountryHistory().refresh();
	}
	
}
