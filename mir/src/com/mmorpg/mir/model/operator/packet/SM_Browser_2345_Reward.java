package com.mmorpg.mir.model.operator.packet;

public class SM_Browser_2345_Reward {
	private int code;

	public static SM_Browser_2345_Reward valueOf(int code) {
		SM_Browser_2345_Reward sm = new SM_Browser_2345_Reward();
		sm.code = code;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
