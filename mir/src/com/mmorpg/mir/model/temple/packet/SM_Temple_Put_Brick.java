package com.mmorpg.mir.model.temple.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Temple_Put_Brick {
	private Reward reward;

	public static SM_Temple_Put_Brick valueOf(Reward reward) {
		SM_Temple_Put_Brick sm = new SM_Temple_Put_Brick();
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
