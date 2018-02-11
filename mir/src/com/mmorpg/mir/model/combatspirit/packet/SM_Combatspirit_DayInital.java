package com.mmorpg.mir.model.combatspirit.packet;

public class SM_Combatspirit_DayInital {

	private String resourceId;
	
	public static SM_Combatspirit_DayInital valueOf(String id) {
		SM_Combatspirit_DayInital sm = new SM_Combatspirit_DayInital();
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
