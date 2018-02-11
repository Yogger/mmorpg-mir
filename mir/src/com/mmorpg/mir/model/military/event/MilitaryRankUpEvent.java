package com.mmorpg.mir.model.military.event;

import com.windforce.common.event.event.IEvent;

public class MilitaryRankUpEvent implements IEvent {

	private long playerId;
	
	private int rank;
	
	
	public static MilitaryRankUpEvent valueOf(long playerId, int rank) {
		MilitaryRankUpEvent event = new MilitaryRankUpEvent();
		event.playerId = playerId;
		event.rank = rank;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
