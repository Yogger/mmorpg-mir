package com.mmorpg.mir.model.operator.packet;

public class SM_MobilePhone_Reward {
	public static SM_MobilePhone_Reward valueOf(int code) {
		SM_MobilePhone_Reward sm = new SM_MobilePhone_Reward();
		sm.code = code;
		return sm;
	}

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
