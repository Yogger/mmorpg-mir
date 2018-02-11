package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

/**
 * 使用国民召集令
 * 
 * @author 37.com
 * 
 */
public class UseCallTogetherEvent implements IEvent {

	private long owner;

	public static UseCallTogetherEvent valueOf(long owner) {
		UseCallTogetherEvent result = new UseCallTogetherEvent();
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
