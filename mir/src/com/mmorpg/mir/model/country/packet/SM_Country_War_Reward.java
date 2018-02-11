package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.reward.model.Reward;

public class SM_Country_War_Reward {

	private int countryValue;
	private int type;
	private int leftCount;
	private Reward reward;
	private byte lost;

	public static SM_Country_War_Reward valueOf(int country, int type, int leftCount, byte lost, Reward reward) {
		SM_Country_War_Reward sm = new SM_Country_War_Reward();
		sm.countryValue = country;
		sm.type = type;
		sm.leftCount = leftCount;
		sm.reward = reward;
		sm.lost = lost;
		return sm;
	}
	
	public static SM_Country_War_Reward valueOf(int country, int type, int leftCount, Reward reward) {
		SM_Country_War_Reward sm = new SM_Country_War_Reward();
		sm.countryValue = country;
		sm.type = type;
		sm.leftCount = leftCount;
		sm.reward = reward;
		return sm;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public byte getLost() {
		return lost;
	}

	public void setLost(byte lost) {
		this.lost = lost;
	}

}
