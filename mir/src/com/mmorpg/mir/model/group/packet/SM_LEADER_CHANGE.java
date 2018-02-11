package com.mmorpg.mir.model.group.packet;

public class SM_LEADER_CHANGE {
	private long playerId;

	public static SM_LEADER_CHANGE valueOf(long playerId) {
		SM_LEADER_CHANGE sm = new SM_LEADER_CHANGE();
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
