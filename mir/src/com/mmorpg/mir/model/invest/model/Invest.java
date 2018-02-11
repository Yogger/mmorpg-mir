package com.mmorpg.mir.model.invest.model;

import java.util.HashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.New;

public class Invest {

	/** 已经领取过的 */
	private HashSet<String> rewarded;

	/** 购买时间 */
	private long buyTime;

	/** 购买后的累计登录天数 */
	private int accLoginDays;

	// 构造函数
	public static Invest valueOf(InvestType type) {
		Invest result = new Invest();
		result.buyTime = System.currentTimeMillis();
		result.rewarded = New.hashSet();
		result.accLoginDays = 1;
		return result;
	}

	@JsonIgnore
	public void addAccLoginDays() {
		this.accLoginDays++;
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

	public int getAccLoginDays() {
		return accLoginDays;
	}

	public void setAccLoginDays(int accLoginDays) {
		this.accLoginDays = accLoginDays;
	}

}
