package com.mmorpg.mir.model.openactive.packet;

public class SM_TodayRecharge {
	private long goldAmount;

	public static SM_TodayRecharge valueOf(long goldAmount) {
		SM_TodayRecharge result = new SM_TodayRecharge();
		result.goldAmount = goldAmount;
		return result;
	}

	public long getGoldAmount() {
		return goldAmount;
	}

	public void setGoldAmount(long goldAmount) {
		this.goldAmount = goldAmount;
	}

}
