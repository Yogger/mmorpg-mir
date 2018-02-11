package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class PlayerKillCountryFlagEvent implements IEvent {

	private long owner;

	public static PlayerKillCountryFlagEvent valueOf(long owner) {
		PlayerKillCountryFlagEvent event = new PlayerKillCountryFlagEvent();
		event.owner = owner;
		return event;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
