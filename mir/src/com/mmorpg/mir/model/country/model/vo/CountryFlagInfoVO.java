package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.Country;

public class CountryFlagInfoVO {
	
	private int countryId;
	private long reliveTime;

	public static CountryFlagInfoVO valueOf(Country country) {
		CountryFlagInfoVO vo = new CountryFlagInfoVO();
		vo.setCountryId(country.getId().getValue());
		vo.setReliveTime(country.getCountryFlag().getReliveTime());
		return vo;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public long getReliveTime() {
		return reliveTime;
	}

	public void setReliveTime(long reliveTime) {
		this.reliveTime = reliveTime;
	}

}
