package com.mmorpg.mir.model.country.packet;

import java.util.Map;

public class SM_Country_Store {

	private Map<Integer, Object> packUpdate;
	private Map<Integer, Object> countryStorageUpdate;

	public static SM_Country_Store valueOf(Map<Integer, Object> packUpdate, Map<Integer, Object> countryStorageUpdate) {
		SM_Country_Store sm = new SM_Country_Store();
		sm.packUpdate = packUpdate;
		sm.countryStorageUpdate = countryStorageUpdate;
		return sm;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public Map<Integer, Object> getCountryStorageUpdate() {
		return countryStorageUpdate;
	}

	public void setCountryStorageUpdate(Map<Integer, Object> countryStorageUpdate) {
		this.countryStorageUpdate = countryStorageUpdate;
	}

}
