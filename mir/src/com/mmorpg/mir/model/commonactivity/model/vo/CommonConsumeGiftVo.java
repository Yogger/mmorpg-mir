package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.commonactivity.model.CommonConsumeGift;

public class CommonConsumeGiftVo {
	private int consumeCount;
	
	private ArrayList<Integer> rewarded;

	public static CommonConsumeGiftVo ValueOf(CommonConsumeGift gift){
		CommonConsumeGiftVo sm = new CommonConsumeGiftVo();
		sm.consumeCount = gift.getConsumeCount();
		sm.rewarded = gift.getRewarded();
		return sm;
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
