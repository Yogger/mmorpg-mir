package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_WarBook_Copy_Reward {
	private String id;
	
	private Reward reward;
	
	public static SM_WarBook_Copy_Reward valueOf(String id, Reward reward){
		SM_WarBook_Copy_Reward sm = new SM_WarBook_Copy_Reward();
		sm.id = id;
		sm.reward = reward;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}
}
