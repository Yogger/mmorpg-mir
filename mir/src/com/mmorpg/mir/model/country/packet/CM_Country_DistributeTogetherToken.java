package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Country_DistributeTogetherToken extends CM_System_Sign {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
