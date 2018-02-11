package com.mmorpg.mir.model.group.packet;

public class SM_GROUP_LEAVE {
	private long playerId;

	public static SM_GROUP_LEAVE valueOf(long playerId) {
		SM_GROUP_LEAVE sm = new SM_GROUP_LEAVE();
		sm.playerId = playerId;
		return sm;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
