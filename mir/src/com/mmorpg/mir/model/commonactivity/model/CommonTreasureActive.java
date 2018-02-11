package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CommonTreasureActive {

	private String activityName;

	private HashSet<String> rewarded;

	private int count;

	public static CommonTreasureActive valueOf(String activityName) {
		CommonTreasureActive result = new CommonTreasureActive();
		result.activityName = activityName;
		result.rewarded = new HashSet<String>();
		return result;
	}

	@JsonIgnore
	public void addCount(int value) {
		this.count += value;
	}

	@JsonIgnore
	public void rewarded(String id) {
		rewarded.add(id);
	}

	@JsonIgnore
	public boolean isRewarded(String id) {
		return this.rewarded.contains(id);
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
