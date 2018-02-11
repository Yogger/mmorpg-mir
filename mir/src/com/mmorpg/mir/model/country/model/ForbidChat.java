package com.mmorpg.mir.model.country.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ForbidChat {
	private long playerId;
	private long endTime;

	public static ForbidChat valueOf(long playerId, long endTime) {
		ForbidChat fc = new ForbidChat();
		fc.playerId = playerId;
		fc.endTime = endTime;
		return fc;
	}

	@JsonIgnore
	public boolean end() {
		if (endTime < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
