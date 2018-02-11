package com.mmorpg.mir.model.vip.model;

public class GoldRechargeHistory {
	private int gold;
	private long time;

	public static GoldRechargeHistory valueOf(int gold, long time) {
		GoldRechargeHistory rh = new GoldRechargeHistory();
		rh.gold = gold;
		rh.time = time;
		return rh;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
