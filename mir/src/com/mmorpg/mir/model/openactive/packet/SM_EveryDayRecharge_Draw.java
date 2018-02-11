package com.mmorpg.mir.model.openactive.packet;

public class SM_EveryDayRecharge_Draw {
	private String resourceId;

	public static SM_EveryDayRecharge_Draw valueOf(String resourceId) {
		SM_EveryDayRecharge_Draw result = new SM_EveryDayRecharge_Draw();
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
