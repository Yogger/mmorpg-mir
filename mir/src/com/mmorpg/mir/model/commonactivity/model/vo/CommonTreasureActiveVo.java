package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.HashSet;

import com.mmorpg.mir.model.commonactivity.model.CommonTreasureActive;

public class CommonTreasureActiveVo {
	private int count;

	private HashSet<String> rewarded;

	private String activityName;

	public static CommonTreasureActiveVo valueOf(CommonTreasureActive active) {
		CommonTreasureActiveVo result = new CommonTreasureActiveVo();
		result.count = active.getCount();
		result.rewarded = active.getRewarded();
		result.activityName = active.getActivityName();
		return result;
	}

	public int getCount() {
		return count;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
