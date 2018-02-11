package com.mmorpg.mir.model.serverstate.facade;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class ServerStateFacade {
	
	@ReceiverAnno
	public void playerLevelUp(LevelUpEvent event) {
		ServerState.getInstance().doLevelReward(event);
	}
}
