package com.mmorpg.mir.model.agateinvest.model;

import java.util.HashSet;

import com.windforce.common.utility.New;

public class InvestAgate {

	/** 已经领取过的 */
	private HashSet<String> rewarded;

	/** 购买时间 */
	private long buyTime;

	// 构造函数
	public static InvestAgate valueOf(InvestAgateType type) {
		InvestAgate result = new InvestAgate();
		result.buyTime = System.currentTimeMillis();
		result.rewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public long getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
}
