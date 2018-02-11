package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Attend_Reward {

	private Reward reward;

	public static SM_Attend_Reward valueOf(Reward r) {
		SM_Attend_Reward sm = new SM_Attend_Reward();
		sm.reward = r;
		return sm;
	}
	
	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}
	
}
