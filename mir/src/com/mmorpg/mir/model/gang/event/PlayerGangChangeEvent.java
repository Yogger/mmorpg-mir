package com.mmorpg.mir.model.gang.event;

import com.windforce.common.event.event.IEvent;

public class PlayerGangChangeEvent implements IEvent {

	private long playerId;
	private long gangId;
	private String gangName;

	@Override
	public long getOwner() {
		return playerId;
	}
	
	public static PlayerGangChangeEvent valueOf(long pid, long gangId, String name) {
		PlayerGangChangeEvent event = new PlayerGangChangeEvent();
		event.gangId = gangId;
		event.playerId = pid;
		event.gangName = name;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

}
