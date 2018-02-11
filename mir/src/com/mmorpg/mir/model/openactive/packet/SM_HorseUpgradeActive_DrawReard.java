package com.mmorpg.mir.model.openactive.packet;

public class SM_HorseUpgradeActive_DrawReard {
	private String resourceId;

	public static SM_HorseUpgradeActive_DrawReard valueOf(String resourceId) {
		SM_HorseUpgradeActive_DrawReard result = new SM_HorseUpgradeActive_DrawReard();
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
