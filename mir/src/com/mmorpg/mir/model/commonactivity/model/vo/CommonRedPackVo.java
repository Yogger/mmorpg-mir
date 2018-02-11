package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.HashSet;

import com.mmorpg.mir.model.commonactivity.model.CommonRedPack;

public class CommonRedPackVo {
	private String activityName;
	private HashSet<String> rewarded;
	private long lastResetTime;

	public static CommonRedPackVo valueOf(CommonRedPack redPack) {
		CommonRedPackVo result = new CommonRedPackVo();
		redPack.refresh();
		result.activityName = redPack.getActivityName();
		result.rewarded = new HashSet<String>(redPack.getRewarded());
		result.lastResetTime = redPack.getLastResetTime();
		return result;
	}

	public String getActivityName() {
		return activityName;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

}
