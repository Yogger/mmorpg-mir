package com.mmorpg.mir.model.operator.packet;

public class SM_WeChat_Reward {
	private int code;

	public static SM_WeChat_Reward valueOf(int code) {
		SM_WeChat_Reward sm = new SM_WeChat_Reward();
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
