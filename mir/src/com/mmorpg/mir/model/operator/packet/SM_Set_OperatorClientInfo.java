package com.mmorpg.mir.model.operator.packet;

public class SM_Set_OperatorClientInfo {
	private int code;

	public static SM_Set_OperatorClientInfo valueOf(int code2) {
		SM_Set_OperatorClientInfo sm = new SM_Set_OperatorClientInfo();
		sm.code = code2;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
