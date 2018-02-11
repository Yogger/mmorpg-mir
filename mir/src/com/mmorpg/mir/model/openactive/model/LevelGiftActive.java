package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import com.windforce.common.utility.New;

public class LevelGiftActive {
	/** 已经领取的等级礼包 */
	private HashSet<String> rewarded = New.hashSet();

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}
}
