package com.mmorpg.mir.model.kingofwar.event;

import com.windforce.common.event.event.IEvent;

public class KingOfKingAbdicateEvent implements IEvent{

	private long playerId;
	
	public static KingOfKingAbdicateEvent valueOf(long originalKingId) {
		KingOfKingAbdicateEvent e = new KingOfKingAbdicateEvent();
		e.playerId = originalKingId;
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
