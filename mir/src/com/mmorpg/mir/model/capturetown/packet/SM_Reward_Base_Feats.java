package com.mmorpg.mir.model.capturetown.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Reward_Base_Feats {
	private Reward reward;

	public static SM_Reward_Base_Feats valueOf(Reward r) {
		SM_Reward_Base_Feats sm = new SM_Reward_Base_Feats();
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
