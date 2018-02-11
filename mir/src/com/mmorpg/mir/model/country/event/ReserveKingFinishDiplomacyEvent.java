package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class ReserveKingFinishDiplomacyEvent implements IEvent {

	private long owner;

	public static ReserveKingFinishDiplomacyEvent valueOf(long owner) {
		ReserveKingFinishDiplomacyEvent result = new ReserveKingFinishDiplomacyEvent();
		result.owner = owner;
		return result;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
