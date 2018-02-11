package com.mmorpg.mir.model.collect.event;

import com.windforce.common.event.event.IEvent;

public class CollectEquipmentEvent implements IEvent {
	
	private long playerId;
	
	public static CollectEquipmentEvent valueOf(long pid) {
		CollectEquipmentEvent event = new CollectEquipmentEvent();
		event.playerId = pid;
		return event;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
