package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Consume_Draw_Reward {
	private String resourceId;

	public static SM_Common_Consume_Draw_Reward valueOf(String id) {
		SM_Common_Consume_Draw_Reward sm = new SM_Common_Consume_Draw_Reward();
		sm.resourceId = id;
		return sm;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
