package com.mmorpg.mir.model.operator.packet;

public class SM_Operator_UnBan {
	private int code;

	public static SM_Operator_UnBan valueOf(int code) {
		SM_Operator_UnBan sm = new SM_Operator_UnBan();
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
