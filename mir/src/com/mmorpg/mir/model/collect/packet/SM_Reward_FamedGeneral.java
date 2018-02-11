package com.mmorpg.mir.model.collect.packet;

public class SM_Reward_FamedGeneral {
	
	private int code;
	
	private String id;
	
	public static SM_Reward_FamedGeneral valueOf(String id) {
		SM_Reward_FamedGeneral sm = new SM_Reward_FamedGeneral();
		sm.id = id;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
