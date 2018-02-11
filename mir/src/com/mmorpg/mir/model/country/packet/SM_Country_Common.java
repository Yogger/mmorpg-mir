package com.mmorpg.mir.model.country.packet;

public class SM_Country_Common {

	public static SM_Country_Common valueOf(int sign) {
		SM_Country_Common sm = new SM_Country_Common();
		sm.sign = sign;
		return sm;
	}

	private int sign;

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

}
