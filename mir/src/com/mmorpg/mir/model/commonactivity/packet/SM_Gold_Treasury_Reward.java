package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Gold_Treasury_Reward {
	private String activeName;

	private int groupId;

	private int index;

	private String rewardId;
	
	private boolean isGoldAction;

	public static SM_Gold_Treasury_Reward valueOf(CM_Gold_Treasury_Reward req) {
		SM_Gold_Treasury_Reward sm = new SM_Gold_Treasury_Reward();
		sm.activeName = req.getActiveName();
		sm.groupId = req.getGroupId();
		sm.index = req.getIndex();
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public boolean isGoldAction() {
		return isGoldAction;
	}

	public void setGoldAction(boolean isGoldAction) {
		this.isGoldAction = isGoldAction;
	}
}
