package com.mmorpg.mir.model.country.event;

import com.windforce.common.event.event.IEvent;

public class CountryTechnologyUpgradeEvent implements IEvent {

	private long playerId;

	public static CountryTechnologyUpgradeEvent valueOf(long pid) {
		CountryTechnologyUpgradeEvent event = new CountryTechnologyUpgradeEvent();
		event.playerId = pid;
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
