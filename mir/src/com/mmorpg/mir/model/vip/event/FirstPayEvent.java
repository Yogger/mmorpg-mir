package com.mmorpg.mir.model.vip.event;

import com.windforce.common.event.event.IEvent;

/**
 * 充值事件
 * 
 * @author 37.com
 * 
 */
public class FirstPayEvent implements IEvent {

	private long playerId;

	public static FirstPayEvent valueOf(long playerId) {
		FirstPayEvent event = new FirstPayEvent();
		event.playerId = playerId;
		return event;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

}
