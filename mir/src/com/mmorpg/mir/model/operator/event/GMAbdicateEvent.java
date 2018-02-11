package com.mmorpg.mir.model.operator.event;

import com.windforce.common.event.event.IEvent;

public class GMAbdicateEvent implements IEvent {

	private long playerId;

	public static GMAbdicateEvent valueOf(long pid) {
		GMAbdicateEvent e = new GMAbdicateEvent();
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
