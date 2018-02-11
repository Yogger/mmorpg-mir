package com.mmorpg.mir.model.commonactivity.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.DateUtils;

public class CommonRedPack {
	private String activityName;
	private Set<String> rewarded;
	private long lastResetTime;

	public static CommonRedPack valueOf(String activityName) {
		CommonRedPack result = new CommonRedPack();
		result.activityName = activityName;
		result.rewarded = new HashSet<String>();
		result.lastResetTime = System.currentTimeMillis();
		return result;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastResetTime))) {
			this.lastResetTime = System.currentTimeMillis();
			this.rewarded.clear();
		}
	}

	@JsonIgnore
	public void rewarded(String id) {
		rewarded.add(id);
	}

	@JsonIgnore
	public boolean isRewarded(String id) {
		return this.rewarded.contains(id);
	}

	public Set<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(Set<String> rewarded) {
		this.rewarded = rewarded;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

}
