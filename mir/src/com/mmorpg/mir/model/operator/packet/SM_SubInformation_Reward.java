package com.mmorpg.mir.model.operator.packet;

public class SM_SubInformation_Reward {
	private int code;

	public static SM_SubInformation_Reward valueOf(int code) {
		SM_SubInformation_Reward sm = new SM_SubInformation_Reward();
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
