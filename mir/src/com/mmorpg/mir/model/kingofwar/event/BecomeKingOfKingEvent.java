
package com.mmorpg.mir.model.kingofwar.event;

import com.windforce.common.event.event.IEvent;

public class BecomeKingOfKingEvent implements IEvent{
	private long playerId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public static BecomeKingOfKingEvent valueOf(long kingOfKingId) {
		BecomeKingOfKingEvent e = new BecomeKingOfKingEvent();
		e.playerId = kingOfKingId;
		return e;
	}

	@Override
	public long getOwner() {
		return playerId;
	}
}
