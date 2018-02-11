package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

/**
 * 储君退位
 * 
 * @author 37.com
 * 
 */
public class ReserveKingAbdicateEvent implements IEvent {
	private long owner;

	public static ReserveKingAbdicateEvent valueOf(long owner) {
		ReserveKingAbdicateEvent result = new ReserveKingAbdicateEvent();
		result.owner = owner;
		return result;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
