package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;

import com.windforce.common.utility.New;

/**
 * 经验礼包领取
 * 
 * @author Kuang Hao
 * @since v1.0 2015-6-4
 * 
 */
public class ExpActive {
	/** 已经领取的经验礼包 */
	private HashSet<String> rewarded = New.hashSet();

	public static ExpActive valueOf() {
		ExpActive result = new ExpActive();
		result.rewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

}
