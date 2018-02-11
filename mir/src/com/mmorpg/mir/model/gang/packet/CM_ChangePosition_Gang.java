package com.mmorpg.mir.model.gang.packet;

public class CM_ChangePosition_Gang {
	private long targetId;

	private int position;

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
