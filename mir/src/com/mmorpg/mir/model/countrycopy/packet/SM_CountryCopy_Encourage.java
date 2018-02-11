package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_CountryCopy_Encourage {
	
	private Reward reward;
	private int leftCount;
	
	public static SM_CountryCopy_Encourage valueOf(Reward r, int count) {
		SM_CountryCopy_Encourage sm = new SM_CountryCopy_Encourage();
		sm.reward = r;
		sm.leftCount = count;
		return sm;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

}
