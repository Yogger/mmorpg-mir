package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Country_DistributeTank extends CM_System_Sign {
	private String name;
	private int tankId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

}
