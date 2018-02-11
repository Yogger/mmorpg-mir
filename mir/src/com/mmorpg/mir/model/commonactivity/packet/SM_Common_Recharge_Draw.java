package com.mmorpg.mir.model.commonactivity.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Common_Recharge_Draw {
	private String activityName;

	private int code;

	private Reward reward;

	public static SM_Common_Recharge_Draw valueOf(String activityName, Reward r) {
		SM_Common_Recharge_Draw sm = new SM_Common_Recharge_Draw();
		sm.activityName = activityName;
		sm.reward = r;
		return sm;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
