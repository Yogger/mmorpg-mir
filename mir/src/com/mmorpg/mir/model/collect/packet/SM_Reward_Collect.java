package com.mmorpg.mir.model.collect.packet;

import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Reward_Collect {

	private HashSet<String> rewardStatus;

	public static SM_Reward_Collect valueOf(Player player) {
		SM_Reward_Collect sm = new SM_Reward_Collect();
		sm.rewardStatus = new HashSet<String>(player.getCollect().getCollectRewarded());
		return sm;
	}

	public HashSet<String> getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(HashSet<String> rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

}
