package com.mmorpg.mir.model.welfare.packet;

public class SM_Welfare_Sign_Reward {

	private int rewardDayIndex;// 领取了累计多少天签到的奖励,-1表示领奖失败

	public static SM_Welfare_Sign_Reward valueOf(int rewardDayIndex) {
		SM_Welfare_Sign_Reward sm = new SM_Welfare_Sign_Reward();
		sm.setRewardDayIndex(rewardDayIndex);
		return sm;
	}

	public int getRewardDayIndex() {
		return rewardDayIndex;
	}

	public void setRewardDayIndex(int rewardDayIndex) {
		this.rewardDayIndex = rewardDayIndex;
	}

}
