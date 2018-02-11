package com.mmorpg.mir.model.invest.packet;

public class SM_Invest_Draw_Reward {

	private String resourceId;

	public static SM_Invest_Draw_Reward valueOf(String resourceId) {
		SM_Invest_Draw_Reward result = new SM_Invest_Draw_Reward();
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
