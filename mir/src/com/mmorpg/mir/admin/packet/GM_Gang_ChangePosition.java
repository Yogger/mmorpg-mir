package com.mmorpg.mir.admin.packet;

public class GM_Gang_ChangePosition {
	private long gangId;
	/**
	 * 玩家的id
	 */
	private long targetId;

	/**
	 * 帮主 Master(1), 副帮主 Assistant(2), 帮众 Member(3);
	 */
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

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

}
