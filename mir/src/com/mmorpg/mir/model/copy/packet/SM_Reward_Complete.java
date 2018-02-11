package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Reward_Complete {
	private Reward reward;

	public static SM_Reward_Complete valueOf(Reward reward) {
		SM_Reward_Complete sm = new SM_Reward_Complete();
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
