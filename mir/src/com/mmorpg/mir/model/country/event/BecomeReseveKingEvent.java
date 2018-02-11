package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

/**
 * 成为储君事件
 * 
 * @author 37.com
 * 
 */
public class BecomeReseveKingEvent implements IEvent {

	private long owner;

	public static BecomeReseveKingEvent valueOf(long owner) {
		BecomeReseveKingEvent result = new BecomeReseveKingEvent();
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
