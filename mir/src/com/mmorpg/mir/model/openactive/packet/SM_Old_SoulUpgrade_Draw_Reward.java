package com.mmorpg.mir.model.openactive.packet;

public class SM_Old_SoulUpgrade_Draw_Reward {
	
	private String resourceId;
	
	public static SM_Old_SoulUpgrade_Draw_Reward valueOf(String code) {
		SM_Old_SoulUpgrade_Draw_Reward sm = new SM_Old_SoulUpgrade_Draw_Reward();
		sm.resourceId = code;
		return sm;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
}
