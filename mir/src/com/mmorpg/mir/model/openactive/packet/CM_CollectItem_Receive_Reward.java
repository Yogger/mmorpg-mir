package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_CollectItem_Receive_Reward extends CM_System_Sign {
	private String resourceId;

	private int count;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
