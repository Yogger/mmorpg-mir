package com.mmorpg.mir.model.welfare.event;

import com.windforce.common.event.event.IEvent;

/**
 *  营救
 * 
 * @author 37wan
 * 
 */
public class RescueEvent implements IEvent {

	private long playerId;

	public static IEvent valueOf(long playerId) {
		RescueEvent event = new RescueEvent();
		event.setPlayerId(playerId);
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

}
