package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Diplomacy;

public class SM_Country_Diplomacy_Reilve_Push {

	private int countryId;
	private long reliveTime;

	private int countryValue;

	public static SM_Country_Diplomacy_Reilve_Push valueOf(int countryValue, Diplomacy diplomacy) {
		SM_Country_Diplomacy_Reilve_Push sm = new SM_Country_Diplomacy_Reilve_Push();
		sm.setReliveTime(diplomacy.getReliveTime());
		sm.setCountryId(diplomacy.getCountry().getId().getValue());
		sm.countryValue = countryValue;
		return sm;
	}

	public long getReliveTime() {
		return reliveTime;
	}

	public void setReliveTime(long reliveTime) {
		this.reliveTime = reliveTime;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

}
