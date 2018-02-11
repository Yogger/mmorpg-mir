package com.mmorpg.mir.model.relive.event;

import com.windforce.common.event.event.IEvent;

public class PlayerReliveEvent implements IEvent {

	private long owner;

	public static PlayerReliveEvent valueOf(long owner) {
		PlayerReliveEvent result = new PlayerReliveEvent();
		result.owner = owner;
		return result;
	}

	@Override
	public long getOwner() {
		return owner;
	}

}
