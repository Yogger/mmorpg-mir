package com.mmorpg.mir.admin.packet;

public class GM_ForbidChatById {
	private long guid;
	private long endTime;

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
