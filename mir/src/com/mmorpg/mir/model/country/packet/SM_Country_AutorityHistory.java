package com.mmorpg.mir.model.country.packet;

public class SM_Country_AutorityHistory {
	private String key;
	private int value;

	public static SM_Country_AutorityHistory valueOf(String key, int value) {
		SM_Country_AutorityHistory sm = new SM_Country_AutorityHistory();
		sm.key = key;
		sm.value = value;
		return sm;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
