package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.system.packet.SM_System_Sign;

public class CM_Military_Active_Reward extends SM_System_Sign{
	private String resourceId;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
