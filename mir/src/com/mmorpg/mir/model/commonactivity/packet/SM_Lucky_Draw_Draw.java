package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Lucky_Draw_Draw {
	private String rewardId;

	public static SM_Lucky_Draw_Draw valueOf(String rewardId) {
		SM_Lucky_Draw_Draw sm = new SM_Lucky_Draw_Draw();
		sm.rewardId = rewardId;
		return sm;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}
}
