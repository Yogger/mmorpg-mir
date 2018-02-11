package com.mmorpg.mir.model.warship.event;

import com.windforce.common.event.event.IEvent;

public class WarshipEvent implements IEvent {

	private long playerId;
	
	public static WarshipEvent valueOf(long pid) {
		WarshipEvent e = new WarshipEvent();
		e.playerId = pid;
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
