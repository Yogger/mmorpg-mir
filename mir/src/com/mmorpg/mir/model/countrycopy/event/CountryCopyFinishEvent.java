package com.mmorpg.mir.model.countrycopy.event;

import com.windforce.common.event.event.IEvent;

public class CountryCopyFinishEvent implements IEvent {

	private long owner;

	public static CountryCopyFinishEvent valueOf(long owner) {
		CountryCopyFinishEvent result = new CountryCopyFinishEvent();
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
