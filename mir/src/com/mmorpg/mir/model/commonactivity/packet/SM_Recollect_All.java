package com.mmorpg.mir.model.commonactivity.packet;

import java.util.HashSet;

public class SM_Recollect_All {
	
	private HashSet<String> rewardIds;
	
	public static SM_Recollect_All valueOf(HashSet<String> rewardIds) {
		SM_Recollect_All sm = new SM_Recollect_All();
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
