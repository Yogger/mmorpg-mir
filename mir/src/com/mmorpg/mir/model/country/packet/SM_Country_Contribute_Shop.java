package com.mmorpg.mir.model.country.packet;

public class SM_Country_Contribute_Shop {

	private int addContribution;
	private int addShopExp;
	private long shopExp;

	public static SM_Country_Contribute_Shop valueOf(long currentShopExp, int addShopExp, int addContribution) {
		SM_Country_Contribute_Shop sm = new SM_Country_Contribute_Shop();
		sm.addContribution = addContribution;
		sm.addShopExp = addShopExp;
		sm.shopExp = currentShopExp;
		return sm;
	}

	public int getAddContribution() {
		return addContribution;
	}

	public void setAddContribution(int addContribution) {
		this.addContribution = addContribution;
	}

	public long getShopExp() {
		return shopExp;
	}

	public void setShopExp(long shopExp) {
		this.shopExp = shopExp;
	}

	public int getAddShopExp() {
		return addShopExp;
	}

	public void setAddShopExp(int addShopExp) {
		this.addShopExp = addShopExp;
	}

}
