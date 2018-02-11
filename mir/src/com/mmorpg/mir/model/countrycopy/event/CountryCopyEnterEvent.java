package com.mmorpg.mir.model.countrycopy.event;

import com.windforce.common.event.event.IEvent;

public class CountryCopyEnterEvent implements IEvent {

	private long owner;

	public static CountryCopyEnterEvent valueOf(long owner) {
		CountryCopyEnterEvent result = new CountryCopyEnterEvent();
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
