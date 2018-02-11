package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Consume_Gift_Reward {
	private String activeName;
	
	private int groupId;

	public static SM_Consume_Gift_Reward ValueOf(CM_Consume_Gift_Reward req){
		SM_Consume_Gift_Reward sm = new SM_Consume_Gift_Reward();
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
