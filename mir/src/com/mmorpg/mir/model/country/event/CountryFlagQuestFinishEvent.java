package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class CountryFlagQuestFinishEvent implements IEvent {

	private long owner;

	private int count;

	public static CountryFlagQuestFinishEvent valueOf(long owner, int count) {
		CountryFlagQuestFinishEvent result = new CountryFlagQuestFinishEvent();
		result.owner = owner;
		return result;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
