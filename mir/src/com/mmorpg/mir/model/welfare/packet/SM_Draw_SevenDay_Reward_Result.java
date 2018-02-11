package com.mmorpg.mir.model.welfare.packet;

import java.util.Map;

public class SM_Draw_SevenDay_Reward_Result {

	private Map<Integer, Boolean> rewardRecord;

	public static SM_Draw_SevenDay_Reward_Result valueOf(Map<Integer, Boolean> rewardRecord) {
		SM_Draw_SevenDay_Reward_Result result = new SM_Draw_SevenDay_Reward_Result();
		result.rewardRecord = rewardRecord;
		return result;
	}

	public Map<Integer, Boolean> getRewardRecord() {
		return rewardRecord;
	}

	public void setRewardRecord(Map<Integer, Boolean> rewardRecord) {
		this.rewardRecord = rewardRecord;
	}

}
