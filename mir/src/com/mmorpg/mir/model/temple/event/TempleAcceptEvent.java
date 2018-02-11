package com.mmorpg.mir.model.temple.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class TempleAcceptEvent implements IEvent {

	private long playerId;
	
	public static TempleAcceptEvent valueOf(Player player) {
		TempleAcceptEvent e = new TempleAcceptEvent();
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
