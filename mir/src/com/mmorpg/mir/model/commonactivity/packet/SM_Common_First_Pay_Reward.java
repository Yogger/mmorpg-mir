package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_First_Pay_Reward {
	private int code;
	
	private String id;

	public static SM_Common_First_Pay_Reward valueOf(String id){
		SM_Common_First_Pay_Reward sm = new SM_Common_First_Pay_Reward();
		sm.setId(id);
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
