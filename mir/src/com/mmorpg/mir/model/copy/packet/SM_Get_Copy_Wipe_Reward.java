package com.mmorpg.mir.model.copy.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Get_Copy_Wipe_Reward {

	private String id;
	
	private Reward finishRewardIds;
	
	private Reward dropRewardIds;

	public static SM_Get_Copy_Wipe_Reward valueOf(String copyId, Reward rewardIds, Reward dropIds) {
		SM_Get_Copy_Wipe_Reward sm = new SM_Get_Copy_Wipe_Reward();
		sm.id = copyId;
		sm.finishRewardIds =rewardIds;
		sm.dropRewardIds = dropIds;
		return sm;
	}

	public Reward getFinishRewardIds() {
		return finishRewardIds;
	}

	public void setFinishRewardIds(Reward finishRewardIds) {
		this.finishRewardIds = finishRewardIds;
	}

	public Reward getDropRewardIds() {
		return dropRewardIds;
	}

	public void setDropRewardIds(Reward dropRewardIds) {
		this.dropRewardIds = dropRewardIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
