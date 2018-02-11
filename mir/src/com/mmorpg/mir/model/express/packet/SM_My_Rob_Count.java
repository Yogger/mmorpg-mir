package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_My_Rob_Count {

	private int robCount;
	private Reward reward;

	public static SM_My_Rob_Count valueOf(int count, Reward r) {
		SM_My_Rob_Count sm = new SM_My_Rob_Count();
		sm.robCount = count;
		sm.reward = r;
		return sm;
	}
	
	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}


	public int getRobCount() {
		return robCount;
	}

	public void setRobCount(int robCount) {
		this.robCount = robCount;
	}
	
}
