package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_CountryCopy_End {

	private Reward reward;

	public static SM_CountryCopy_End valueOf(Reward r) {
		SM_CountryCopy_End sm = new SM_CountryCopy_End();
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
