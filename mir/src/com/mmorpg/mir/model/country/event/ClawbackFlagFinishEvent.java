package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class ClawbackFlagFinishEvent implements IEvent {

	private int count;
	
	private long playerId;
	
	public static ClawbackFlagFinishEvent valueOf(long pid, int c) {
		ClawbackFlagFinishEvent event = new ClawbackFlagFinishEvent();
		event.playerId = pid;
		event.count = c;
		return event;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
