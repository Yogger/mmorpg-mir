package com.mmorpg.mir.model.country.packet;

public class SM_Country_War_Event {

	private byte country;
	
	private byte value;

	public static SM_Country_War_Event valueOf(int countryValue, int v) {
		SM_Country_War_Event e = new SM_Country_War_Event();
		e.value = (byte) v;
		e.country = (byte) countryValue;
		return e;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

}
