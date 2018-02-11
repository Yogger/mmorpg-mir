package com.mmorpg.mir.model.country.packet;

public class SM_Country_UpgradeDoor {
	private String doorId;

	public static SM_Country_UpgradeDoor valueOf(String doorId) {
		SM_Country_UpgradeDoor door = new SM_Country_UpgradeDoor();
		door.doorId = doorId;
		return door;
	}

	public String getDoorId() {
		return doorId;
	}

	public void setDoorId(String doorId) {
		this.doorId = doorId;
	}
}
