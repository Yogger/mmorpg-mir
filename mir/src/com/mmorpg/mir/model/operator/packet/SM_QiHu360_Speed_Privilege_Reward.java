package com.mmorpg.mir.model.operator.packet;

public class SM_QiHu360_Speed_Privilege_Reward {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static SM_QiHu360_Speed_Privilege_Reward valueOf(int code){
		SM_QiHu360_Speed_Privilege_Reward sm  = new SM_QiHu360_Speed_Privilege_Reward();
		sm.code = code;
		return sm;
	}
}
