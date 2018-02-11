package com.mmorpg.mir.model.openactive.packet;

public class SM_OpenActive_Reward {

	private int sign;
	private String resourceId;
	
	public static SM_OpenActive_Reward valueOf(int sign, String resourceId) {
		SM_OpenActive_Reward sm = new SM_OpenActive_Reward();
		sm.sign = sign;
		sm.resourceId = resourceId;
		return sm;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
