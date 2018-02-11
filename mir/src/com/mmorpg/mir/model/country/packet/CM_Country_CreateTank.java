package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Country_CreateTank extends CM_System_Sign {

	private int index;
	private String resourceId;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
