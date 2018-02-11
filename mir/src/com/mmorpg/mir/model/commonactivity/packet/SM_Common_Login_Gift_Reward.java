package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Login_Gift_Reward {

	private String id;

	public static SM_Common_Login_Gift_Reward valueOf(String id) {
		SM_Common_Login_Gift_Reward sm = new SM_Common_Login_Gift_Reward();
		sm.id = id;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
