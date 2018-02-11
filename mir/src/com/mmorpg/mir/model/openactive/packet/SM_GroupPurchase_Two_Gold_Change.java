package com.mmorpg.mir.model.openactive.packet;

public class SM_GroupPurchase_Two_Gold_Change {
	private long gold;

	public static SM_GroupPurchase_Two_Gold_Change valueOf(long gold) {
		SM_GroupPurchase_Two_Gold_Change result = new SM_GroupPurchase_Two_Gold_Change();
		result.gold = gold;
		return result;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

}
