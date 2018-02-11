package com.mmorpg.mir.model.operator.packet;

public class SM_Operator_Hide {
	private int code;

	public static SM_Operator_Hide valueOf(int code) {
		SM_Operator_Hide sm = new SM_Operator_Hide();
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
