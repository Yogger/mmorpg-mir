package com.mmorpg.mir.model.operator.packet;

public class SM_Operator_ShowGMNickname {
	private int code;

	public static SM_Operator_ShowGMNickname valueOf(int code) {
		SM_Operator_ShowGMNickname sm = new SM_Operator_ShowGMNickname();
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
