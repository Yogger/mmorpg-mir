package com.mmorpg.mir.model.copy.event;

import com.windforce.common.event.event.IEvent;

public class CopyCompleteEvent implements IEvent {

	public static IEvent valueOf(long playerId, String copyId) {
		CopyCompleteEvent event = new CopyCompleteEvent();
		event.playerId = playerId;
		event.copyId = copyId;
		return event;
	}

	private long playerId;
	private String copyId;

	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

}