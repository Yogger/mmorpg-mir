package com.mmorpg.mir.model.mergeactive.packet;

public class SM_Draw_Merge_Consume {

	private String resourceId;

	public static SM_Draw_Merge_Consume valueOf(String id) {
		SM_Draw_Merge_Consume sm = new SM_Draw_Merge_Consume();
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
