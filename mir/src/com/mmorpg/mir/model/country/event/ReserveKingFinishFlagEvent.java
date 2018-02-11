package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class ReserveKingFinishFlagEvent implements IEvent {

	private long owner;

	public static ReserveKingFinishFlagEvent valueOf(long owner) {
		ReserveKingFinishFlagEvent result = new ReserveKingFinishFlagEvent();
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
