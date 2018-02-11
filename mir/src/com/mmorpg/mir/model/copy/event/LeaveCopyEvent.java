package com.mmorpg.mir.model.copy.event;

import com.windforce.common.event.event.IEvent;

public class LeaveCopyEvent implements IEvent {

	public static final String EVENT_NAME = "copy:leave_copy";

	public static IEvent valueOf(long playerId, String copyId) {
		LeaveCopyEvent event = new LeaveCopyEvent();
		event.playerId = playerId;
		event.copyId = copyId;
		return event;
	}

	private long playerId;
	private String copyId;

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
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