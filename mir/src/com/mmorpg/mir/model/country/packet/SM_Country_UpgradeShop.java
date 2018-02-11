package com.mmorpg.mir.model.country.packet;

public class SM_Country_UpgradeShop {

	private int level;
	private long shopExp;

	public static SM_Country_UpgradeShop valueOf(int level, long shopExp) {
		SM_Country_UpgradeShop sm = new SM_Country_UpgradeShop();
		sm.level = level;
		sm.shopExp = shopExp;
		return sm;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getShopExp() {
		return shopExp;
	}

	public void setShopExp(long shopExp) {
		this.shopExp = shopExp;
	}

}
