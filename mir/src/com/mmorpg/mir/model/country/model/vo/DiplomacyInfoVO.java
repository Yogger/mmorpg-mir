package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.Country;

public class DiplomacyInfoVO {

	private int countryId;
	private long reliveTime;

	public static DiplomacyInfoVO valueOf(Country country) {
		DiplomacyInfoVO vo = new DiplomacyInfoVO();
		vo.setCountryId(country.getId().getValue());
		vo.setReliveTime(country.getDiplomacy().getReliveTime());
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
