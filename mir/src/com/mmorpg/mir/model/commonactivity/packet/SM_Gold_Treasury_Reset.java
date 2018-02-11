package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Gold_Treasury_Reset {
	private String activeName;

	private int groupId;

	public static SM_Gold_Treasury_Reset valueOf(CM_Gold_Treasury_Reset req) {
		SM_Gold_Treasury_Reset sm = new SM_Gold_Treasury_Reset();
		sm.activeName = req.getActiveName();
		sm.groupId = req.getGroupId();
		return sm;
	}

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
