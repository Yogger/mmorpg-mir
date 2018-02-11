package com.mmorpg.mir.model.openactive.packet;

public class SM_GroupPurchase_Two_Reward {
	private String resourceId;

	public static SM_GroupPurchase_Two_Reward valueOf(String resourceId) {
		SM_GroupPurchase_Two_Reward result = new SM_GroupPurchase_Two_Reward();
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
