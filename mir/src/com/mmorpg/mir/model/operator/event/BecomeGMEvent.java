package com.mmorpg.mir.model.operator.event;

import com.windforce.common.event.event.IEvent;

public class BecomeGMEvent implements IEvent{

	private long playerId;
	
	public static BecomeGMEvent valueOf(long pid) {
		BecomeGMEvent e = new BecomeGMEvent();
		e.playerId = pid;
		return e;
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
