package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Consume_Active_Reward extends CM_System_Sign {

	private String resourceId;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
