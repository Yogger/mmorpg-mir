package com.mmorpg.mir.model.capturetown;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class TownCaptureEvent implements IEvent{

	private long playerId;

	public static TownCaptureEvent valueOf(Player player) {
		TownCaptureEvent e = new TownCaptureEvent();
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
