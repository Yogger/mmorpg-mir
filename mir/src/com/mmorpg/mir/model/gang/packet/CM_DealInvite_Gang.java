package com.mmorpg.mir.model.gang.packet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CM_DealInvite_Gang {
	private long gangId;
	private byte ok;

	@JsonIgnore
	public boolean isOk() {
		if (ok == 0) {
			return false;
		}
		return true;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public byte getOk() {
		return ok;
	}

	public void setOk(byte ok) {
		this.ok = ok;
	}

}
