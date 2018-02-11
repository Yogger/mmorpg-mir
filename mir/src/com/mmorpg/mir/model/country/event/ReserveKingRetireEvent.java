package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class ReserveKingRetireEvent implements IEvent{

	private long playerId;
	
	public static ReserveKingRetireEvent valueOf(long pid) {
		ReserveKingRetireEvent e = new ReserveKingRetireEvent();
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
