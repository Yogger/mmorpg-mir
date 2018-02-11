package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CommonLoginGift {

	private HashSet<String> rewarded;

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public static CommonLoginGift valueOf() {
		CommonLoginGift gift = new CommonLoginGift();
		gift.rewarded = new HashSet<String>();
		return gift;
	}

	@JsonIgnore
	public boolean hasDrawBefore(String id) {
		return rewarded.contains(id);
	}

	@JsonIgnore
	public void addDrawLog(String id) {
		rewarded.add(id);
	}
}
