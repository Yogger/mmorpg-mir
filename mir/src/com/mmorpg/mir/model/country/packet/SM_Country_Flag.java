package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.CountryFlag;

public class SM_Country_Flag {
	private String flagId;

	public static SM_Country_Flag valueOf(CountryFlag flag) {
		SM_Country_Flag sm = new SM_Country_Flag();
		sm.flagId = flag.getFlagId();
		return sm;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

}
