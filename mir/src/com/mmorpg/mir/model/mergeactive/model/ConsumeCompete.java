package com.mmorpg.mir.model.mergeactive.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class ConsumeCompete {

	// 活动期间消费的元宝
	private int consumeGold;

	private HashSet<String> rewarded = New.hashSet();

	public static ConsumeCompete valueOf() {
		ConsumeCompete cpe = new ConsumeCompete();
		return cpe;
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
	
	@JsonIgnore
	public int addConsumeGold(int addValue) {
		consumeGold += addValue;
		return consumeGold;
	}
	
}
