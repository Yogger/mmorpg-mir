package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.CountryId;

public class SM_Country_Reset {

	private String countryId;

	public static SM_Country_Reset valueOf(CountryId countryId) {
		SM_Country_Reset sm = new SM_Country_Reset();
		sm.setCountryId(countryId.name());
		return sm;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

}
