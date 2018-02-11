package com.mmorpg.mir.admin.packet;

public class SGM_BlackShop_Close {
	private int code;

	public static SGM_BlackShop_Close valueOf() {
		SGM_BlackShop_Close result = new SGM_BlackShop_Close();
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
