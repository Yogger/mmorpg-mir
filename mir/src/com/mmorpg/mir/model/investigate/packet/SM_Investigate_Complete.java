package com.mmorpg.mir.model.investigate.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Investigate_Complete {
	private String id;

	private Reward reward;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static SM_Investigate_Complete valueOf(String id, Reward reward) {
		SM_Investigate_Complete sm = new SM_Investigate_Complete();
		sm.id = id;
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
