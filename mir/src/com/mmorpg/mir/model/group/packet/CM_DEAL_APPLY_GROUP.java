package com.mmorpg.mir.model.group.packet;

public class CM_DEAL_APPLY_GROUP {
	private long playerId;
	private byte ok;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public byte getOk() {
		return ok;
	}

	public void setOk(byte ok) {
		this.ok = ok;
	}
}
