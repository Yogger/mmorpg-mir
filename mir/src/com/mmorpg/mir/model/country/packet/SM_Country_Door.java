package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Technology;

public class SM_Country_Door {
	private String doorId;

	public static SM_Country_Door valueOf(Technology technology) {
		SM_Country_Door sm = new SM_Country_Door();
		sm.doorId = technology.getDoorId();
		return sm;
	}

	public String getDoorId() {
		return doorId;
	}

	public void setDoorId(String doorId) {
		this.doorId = doorId;
	}

}
