package com.mmorpg.mir.model.country.packet;

import java.util.Collection;
import java.util.Map;

import com.mmorpg.mir.model.country.model.Country;
import com.windforce.common.utility.New;

public class SM_All_CountryStatus {

	private Map<Integer, Long> expressEndTimes;
	private Map<Integer, Long> templeEndTimes;

	public static SM_All_CountryStatus valueOf(Collection<Country> countries) {
		SM_All_CountryStatus sm = new SM_All_CountryStatus();
		Map<Integer, Long> expressEnds = New.hashMap();
		Map<Integer, Long> templeEnds = New.hashMap();
		for (Country country: countries) {
			expressEnds.put(country.getId().getValue(), country.getCountryQuest().getExpressEndTime());
			templeEnds.put(country.getId().getValue(), country.getCountryQuest().getTempleEndTime());
		}
		sm.expressEndTimes = expressEnds;
		sm.templeEndTimes = templeEnds;
		return sm;
	}
	
	public Map<Integer, Long> getExpressEndTimes() {
		return expressEndTimes;
	}

	public void setExpressEndTimes(Map<Integer, Long> expressEndTimes) {
		this.expressEndTimes = expressEndTimes;
	}

	public Map<Integer, Long> getTempleEndTimes() {
		return templeEndTimes;
	}

	public void setTempleEndTimes(Map<Integer, Long> templeEndTimes) {
		this.templeEndTimes = templeEndTimes;
	}

}
