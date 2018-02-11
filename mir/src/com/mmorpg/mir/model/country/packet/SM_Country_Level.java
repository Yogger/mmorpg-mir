package com.mmorpg.mir.model.country.packet;

public class SM_Country_Level {

	private int countryLevel;
	
	public static SM_Country_Level valueOf(int level) {
		SM_Country_Level sm = new SM_Country_Level();
		sm.countryLevel = level;
		return sm;
	}

	public int getCountryLevel() {
		return countryLevel;
	}

	public void setCountryLevel(int countryLevel) {
		this.countryLevel = countryLevel;
	}

}
