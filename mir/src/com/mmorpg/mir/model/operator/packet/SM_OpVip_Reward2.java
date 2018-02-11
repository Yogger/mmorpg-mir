package com.mmorpg.mir.model.operator.packet;

public class SM_OpVip_Reward2 {
	private Integer id;
	
	private int code;

	public static SM_OpVip_Reward2 valueOf(Integer id, int code){
		SM_OpVip_Reward2 sm = new SM_OpVip_Reward2();
		sm.id = id;
		sm.code = code;
		return sm;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
