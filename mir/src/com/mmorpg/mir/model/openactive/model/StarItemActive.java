package com.mmorpg.mir.model.openactive.model;

/**
 * 星级套装
 * 
 * @author 37.com
 * 
 */
public class StarItemActive {
	/** 是否已领奖 */
	private boolean rewarded;

	public static StarItemActive valueOf() {
		StarItemActive result = new StarItemActive();
		result.rewarded = false;
		return result;
	}

	public boolean isRewarded() {
		return rewarded;
	}

	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}

}
