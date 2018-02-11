package com.mmorpg.mir.model.rescue.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class RescueStartEvent implements IEvent{

	private long playerId;
	
	public static RescueStartEvent valueOf(Player player) {
		RescueStartEvent s = new RescueStartEvent();
		s.playerId = player.getObjectId();
		return s;
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
