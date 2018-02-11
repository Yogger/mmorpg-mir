package com.mmorpg.mir.model.country.packet;

public class SM_CountryNpc_Rank_Sign {
	private byte sign;
	private byte country;

	public static SM_CountryNpc_Rank_Sign valueOf(byte s, int c) {
		SM_CountryNpc_Rank_Sign sm = new SM_CountryNpc_Rank_Sign();
		sm.sign = s;
		sm.country = (byte) c;
		return sm;
	}

	public byte getSign() {
		return sign;
	}

	public void setSign(byte sign) {
		this.sign = sign;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

}
