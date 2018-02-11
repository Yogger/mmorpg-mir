package com.mmorpg.mir.model.commonactivity.model;

import java.util.LinkedList;

public class CommonFirstPay {
	private long payCount;

	private LinkedList<String> rewardIds;

	public static CommonFirstPay valueOf() {
		CommonFirstPay pay = new CommonFirstPay();
		pay.payCount = 0L;
		pay.rewardIds = new LinkedList<String>();
		return pay;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public LinkedList<String> getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(LinkedList<String> rewardIds) {
		this.rewardIds = rewardIds;
	}

	public void addPayCount(long count) {
		payCount += count;
	}
	
	public boolean hasDrawBefore(String id){
		return rewardIds.contains(id);
	}
	
	public void addRewardLog(String id){
		rewardIds.add(id);
	}
}
