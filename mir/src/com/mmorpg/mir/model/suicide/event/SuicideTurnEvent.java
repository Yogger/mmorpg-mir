package com.mmorpg.mir.model.suicide.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class SuicideTurnEvent implements IEvent {

	private long owner;

	public static SuicideTurnEvent valueOf(Player player) {
		SuicideTurnEvent event = new SuicideTurnEvent();
		event.owner = player.getObjectId();
		return event;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

}
