package com.mmorpg.mir.model.system.packet;

public class SM_System_Sign {

	public static SM_System_Sign valueOf(int sign) {
		SM_System_Sign sm = new SM_System_Sign();
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
