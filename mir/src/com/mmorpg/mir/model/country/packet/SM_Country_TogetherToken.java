package com.mmorpg.mir.model.country.packet;

public class SM_Country_TogetherToken {

	public static SM_Country_TogetherToken valueOf(byte token, String name) {
		SM_Country_TogetherToken sm = new SM_Country_TogetherToken();
		sm.token = token;
		sm.name = name;
		return sm;
	}

	private byte token;
	private String name;

	public byte getToken() {
		return token;
	}

	public void setToken(byte token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
