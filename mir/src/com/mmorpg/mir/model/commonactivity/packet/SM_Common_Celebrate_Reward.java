package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Celebrate_Reward {
	private String id;

	public static SM_Common_Celebrate_Reward valueOf(String id) {
		SM_Common_Celebrate_Reward result = new SM_Common_Celebrate_Reward();
		result.id = id;
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
