package com.mmorpg.mir.model.country.packet;

public class SM_Country_Close_Event {

	private byte value;
	
	private byte country;

	public static SM_Country_Close_Event valueOf(int eventId, int country) {
		SM_Country_Close_Event e = new SM_Country_Close_Event();
		e.value = (byte) eventId;
		e.country = (byte) country;
		return e;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

}
