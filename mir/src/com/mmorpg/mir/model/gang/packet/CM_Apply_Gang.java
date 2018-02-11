package com.mmorpg.mir.model.gang.packet;

public class CM_Apply_Gang {
	private long id;
	private boolean byPlayerId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isByPlayerId() {
    	return byPlayerId;
    }

	public void setByPlayerId(boolean byPlayerId) {
    	this.byPlayerId = byPlayerId;
    }

}
