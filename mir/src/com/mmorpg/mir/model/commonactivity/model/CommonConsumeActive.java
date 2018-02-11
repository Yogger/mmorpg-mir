package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class CommonConsumeActive {

	private String activityName;
	// 活动期间消费的元宝
	private int consumeGold;

	private HashSet<String> rewarded;

	public static CommonConsumeActive valueOf(String activityName) {
		CommonConsumeActive result = new CommonConsumeActive();
		result.activityName = activityName;
		result.consumeGold = 0;
		result.rewarded = New.hashSet();
		return result;
	}

	@JsonIgnore
	public int addConsumeGold(int addValue) {
		consumeGold += addValue;
		return consumeGold;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
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

}
