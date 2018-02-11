package com.mmorpg.mir.admin.packet;

public class GM_Gang_Expel {
	private long gangId;
	private long targetId;

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}
}
