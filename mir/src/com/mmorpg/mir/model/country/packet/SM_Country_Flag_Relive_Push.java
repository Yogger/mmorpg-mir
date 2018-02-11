package com.mmorpg.mir.model.country.packet;

public class SM_Country_Flag_Relive_Push {

	private int countryId;
	private long reliveTime;
	
	private int hiterCountryValue;

	public static SM_Country_Flag_Relive_Push valueOf(int countryValue, long reliveTime, int cid) {
		SM_Country_Flag_Relive_Push sm = new SM_Country_Flag_Relive_Push();
		sm.reliveTime = reliveTime;
		sm.countryId = cid;
		sm.hiterCountryValue = countryValue;
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

	public int getHiterCountryValue() {
    	return hiterCountryValue;
    }

	public void setHiterCountryValue(int hiterCountryValue) {
    	this.hiterCountryValue = hiterCountryValue;
    }

}
