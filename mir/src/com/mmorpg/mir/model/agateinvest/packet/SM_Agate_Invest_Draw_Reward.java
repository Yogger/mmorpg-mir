package com.mmorpg.mir.model.agateinvest.packet;

public class SM_Agate_Invest_Draw_Reward {

	private String resourceId;

	public static SM_Agate_Invest_Draw_Reward valueOf(String resourceId) {
		SM_Agate_Invest_Draw_Reward result = new SM_Agate_Invest_Draw_Reward();
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
