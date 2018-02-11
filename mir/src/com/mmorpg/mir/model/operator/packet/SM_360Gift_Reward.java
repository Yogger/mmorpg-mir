package com.mmorpg.mir.model.operator.packet;

public class SM_360Gift_Reward {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static SM_360Gift_Reward valueOf(int code2) {
		SM_360Gift_Reward sm = new SM_360Gift_Reward();
		sm.code = code2;
		return sm;
	}
}
