package com.mmorpg.mir.model.copy.event;

import com.windforce.common.event.event.IEvent;

public class LadderResetEvent implements IEvent {

	private long playerId;
	
	public static LadderResetEvent valueOf(Long pid) {
		LadderResetEvent event = new LadderResetEvent();
		event.playerId = pid;
		return event;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public final long getPlayerId() {
		return playerId;
	}

	public final void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	

}
