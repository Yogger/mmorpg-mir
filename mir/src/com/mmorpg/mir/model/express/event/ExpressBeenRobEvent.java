package com.mmorpg.mir.model.express.event;

import com.windforce.common.event.event.IEvent;

public class ExpressBeenRobEvent implements IEvent {

	private long playerId;
	private int lorryCountry;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public static ExpressBeenRobEvent valueOf(long pid, int countryValue) {
		ExpressBeenRobEvent event = new ExpressBeenRobEvent();
		event.playerId = pid;
		event.lorryCountry = countryValue;
		return event;
	}

	public int getLorryCountry() {
    	return lorryCountry;
    }

	public void setLorryCountry(int lorryCountry) {
    	this.lorryCountry = lorryCountry;
    }

}
