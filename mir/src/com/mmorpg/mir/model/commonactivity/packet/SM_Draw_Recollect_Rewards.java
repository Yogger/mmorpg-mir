package com.mmorpg.mir.model.commonactivity.packet;

import java.util.HashSet;

public class SM_Draw_Recollect_Rewards {
	
	private HashSet<String> rewardIds;
	
	public static SM_Draw_Recollect_Rewards valueOf(HashSet<String> rewardIds) {
		SM_Draw_Recollect_Rewards sm = new SM_Draw_Recollect_Rewards();
		sm.rewardIds = rewardIds;
		return sm;
	}

	public HashSet<String> getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(HashSet<String> rewardIds) {
		this.rewardIds = rewardIds;
	}
	
}
