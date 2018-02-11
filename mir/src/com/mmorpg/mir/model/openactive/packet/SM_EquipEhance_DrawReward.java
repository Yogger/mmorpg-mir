package com.mmorpg.mir.model.openactive.packet;

public class SM_EquipEhance_DrawReward {
	private String resourceId;

	public static SM_EquipEhance_DrawReward valueOf(String resourceId) {
		SM_EquipEhance_DrawReward result = new SM_EquipEhance_DrawReward();
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
