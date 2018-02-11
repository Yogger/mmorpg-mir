package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

public class ActiveValueRewardEvent implements IEvent {

	private long owner;

	public static ActiveValueRewardEvent valueOf(long owner) {
		ActiveValueRewardEvent result = new ActiveValueRewardEvent();
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
