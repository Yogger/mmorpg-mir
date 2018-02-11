package com.mmorpg.mir.model.country.packet;

public class SM_Country_OfficalContexts {
	private int key;
	private String value;

	public static SM_Country_OfficalContexts valueOf(int key, String value) {
		SM_Country_OfficalContexts sm = new SM_Country_OfficalContexts();
		sm.key = key;
		sm.value = value;
		return sm;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
