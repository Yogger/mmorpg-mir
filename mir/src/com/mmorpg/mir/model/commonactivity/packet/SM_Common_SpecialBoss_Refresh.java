package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_SpecialBoss_Refresh {

	private String resourceId;

	public static SM_Common_SpecialBoss_Refresh valueOf(String resourceId) {
		SM_Common_SpecialBoss_Refresh result = new SM_Common_SpecialBoss_Refresh();
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
