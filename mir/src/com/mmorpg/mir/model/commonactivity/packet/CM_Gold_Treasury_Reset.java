package com.mmorpg.mir.model.commonactivity.packet;

public class CM_Gold_Treasury_Reset {
	private String activeName;
	
	private int groupId;

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
