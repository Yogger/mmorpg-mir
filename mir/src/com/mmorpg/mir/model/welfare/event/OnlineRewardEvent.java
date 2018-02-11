package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

public class OnlineRewardEvent implements IEvent {

	private long owner;

	public static OnlineRewardEvent valueOf(long owner) {
		OnlineRewardEvent result = new OnlineRewardEvent();
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
