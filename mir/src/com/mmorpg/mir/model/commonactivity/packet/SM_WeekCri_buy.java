package com.mmorpg.mir.model.commonactivity.packet;

public class SM_WeekCri_buy {
	private int sign;

	public static SM_WeekCri_buy valueOf(int sign) {
		SM_WeekCri_buy result = new SM_WeekCri_buy();
		result.sign = sign;
		return result;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}
}
