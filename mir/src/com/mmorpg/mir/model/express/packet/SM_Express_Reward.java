package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Express_Reward {
	private Reward reward;
	private boolean isRobed;

	
	public boolean isRobed() {
		return isRobed;
	}

	public void setRobed(boolean isRobed) {
		this.isRobed = isRobed;
	}

	public static SM_Express_Reward valueOf(Reward reward, boolean rob) {
		SM_Express_Reward sm = new SM_Express_Reward();
		sm.reward = reward;
		sm.isRobed = rob;
		return sm;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}
}
