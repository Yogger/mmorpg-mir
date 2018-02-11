package com.mmorpg.mir.model.operator.packet;

public class SM_QiHu360_Privilege_Reward {
	private int code;
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static SM_QiHu360_Privilege_Reward valueOf(int code, String id){
		SM_QiHu360_Privilege_Reward res = new SM_QiHu360_Privilege_Reward();
		res.code = code;
		res.id = id;
		return res;
	}
}
