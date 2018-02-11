package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class PlayerKillDiplomacyEvent implements IEvent {

	private long owner;

	public static PlayerKillDiplomacyEvent valueOf(long owner) {
		PlayerKillDiplomacyEvent event = new PlayerKillDiplomacyEvent();
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
