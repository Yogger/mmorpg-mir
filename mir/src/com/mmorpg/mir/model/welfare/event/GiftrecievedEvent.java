package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

public class GiftrecievedEvent implements IEvent {

	private long playerId;
	
	public static GiftrecievedEvent valueOf(long pid) {
		GiftrecievedEvent event = new GiftrecievedEvent();
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
