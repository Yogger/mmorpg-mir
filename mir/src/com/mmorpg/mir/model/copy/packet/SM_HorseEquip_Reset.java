package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_HorseEquip_Reset {
	private String id;
	private Reward reward;
	private Reward dropRewardIds;

	public static SM_HorseEquip_Reset valueOf(String id, Reward r, Reward dropRewardIds) {
		SM_HorseEquip_Reset result = new SM_HorseEquip_Reset();
		result.id = id;
		result.reward = r;
		result.dropRewardIds = dropRewardIds;
		return result;
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

	public Reward getDropRewardIds() {
		return dropRewardIds;
	}

	public void setDropRewardIds(Reward dropRewardIds) {
		this.dropRewardIds = dropRewardIds;
	}

}
