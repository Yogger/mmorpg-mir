package com.mmorpg.mir.model.gang.packet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CM_DealApply_Gang {
	private long playerId;
	private byte ok;

	@JsonIgnore
	public boolean isOk() {
		if (ok == 0) {
			return false;
		}
		return true;
	}

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
