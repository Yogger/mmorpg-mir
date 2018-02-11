package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

public class CountrySacrificeEvent implements IEvent{
	
	private long playerId;
	
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

	public static CountrySacrificeEvent valueOf(long pid) {
		CountrySacrificeEvent event = new CountrySacrificeEvent();
		event.playerId = pid;
		return event;
	}
}
