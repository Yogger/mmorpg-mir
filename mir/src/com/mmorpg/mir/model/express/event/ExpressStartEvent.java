package com.mmorpg.mir.model.express.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class ExpressStartEvent implements IEvent {

	private long playerId;
	
	public static ExpressStartEvent valueOf(Player player) {
		ExpressStartEvent e = new ExpressStartEvent();
		e.playerId = player.getObjectId();
		return e;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
