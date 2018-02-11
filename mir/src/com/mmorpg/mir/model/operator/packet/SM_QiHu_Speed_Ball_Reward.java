package com.mmorpg.mir.model.operator.packet;

public class SM_QiHu_Speed_Ball_Reward {
	private int code;

	public static SM_QiHu_Speed_Ball_Reward valueOf(int code) {
		SM_QiHu_Speed_Ball_Reward sm = new SM_QiHu_Speed_Ball_Reward();
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
