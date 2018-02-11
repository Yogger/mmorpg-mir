package com.mmorpg.mir.model.openactive.packet;

public class SM_GroupPurchase_Three_Reward {
	private String resourceId;

	public static SM_GroupPurchase_Three_Reward valueOf(String resourceId) {
		SM_GroupPurchase_Three_Reward result = new SM_GroupPurchase_Three_Reward();
		result.resourceId = resourceId;
		return result;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
