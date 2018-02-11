package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class WeakCountryEvent implements IEvent{
	private long playerId;
	
	public static WeakCountryEvent valueOf(long pid) {
		WeakCountryEvent e = new WeakCountryEvent();
		e.playerId = pid;
		return e;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getOwner() {
		return playerId;
	}
	
}
