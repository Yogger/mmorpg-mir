package com.mmorpg.mir.model.rescue.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Rescue_Reward {
	
	private Reward reward;

	public static SM_Rescue_Reward valueOf(Reward reward) {
		SM_Rescue_Reward sm = new SM_Rescue_Reward();
		sm.reward = reward;
		return sm;
	}
	
	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}
	
}
