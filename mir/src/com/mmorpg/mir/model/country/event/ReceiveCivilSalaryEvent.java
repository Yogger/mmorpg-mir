package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class ReceiveCivilSalaryEvent implements IEvent {

	private long owner;

	public static ReceiveCivilSalaryEvent valueOf(long owner) {
		ReceiveCivilSalaryEvent result = new ReceiveCivilSalaryEvent();
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
