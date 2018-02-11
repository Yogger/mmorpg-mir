package com.mmorpg.mir.model.openactive.packet;

public class SM_GroupPurchase_Attend_Satisfy {

	private String resourceId;

	private int attendAmount;

	public static SM_GroupPurchase_Attend_Satisfy valueOf(String resourceId, int attendAmount) {
		SM_GroupPurchase_Attend_Satisfy result = new SM_GroupPurchase_Attend_Satisfy();
		result.resourceId = resourceId;
		result.attendAmount = attendAmount;
		return result;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getAttendAmount() {
		return attendAmount;
	}

	public void setAttendAmount(int attendAmount) {
		this.attendAmount = attendAmount;
	}

}
