package com.mmorpg.mir.model.commonactivity.model;

import java.util.ArrayList;

public class CommonConsumeGift {
	private int consumeCount;
	
	private ArrayList<Integer> rewarded;

	public static CommonConsumeGift ValueOf(){
		CommonConsumeGift sm = new CommonConsumeGift();
		sm.consumeCount = 0;
		sm.rewarded = new ArrayList<Integer>();
		return sm;
	}
	
	public boolean hasDrawBefore(int groupId){
		return rewarded.contains(groupId);
	}
	
	public void addRewardLog(int groupId){
		rewarded.add(groupId);
	}
	
	public void addConsumeCount(int count){
		consumeCount += count;
	}
	
	public int getConsumeCount() {
		return consumeCount;
	}

	public void setConsumeCount(int consumeCount) {
		this.consumeCount = consumeCount;
	}

	public ArrayList<Integer> getRewarded() {
		return rewarded;
	}

	public void setRewarded(ArrayList<Integer> rewarded) {
		this.rewarded = rewarded;
	}
}
