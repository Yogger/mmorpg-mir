package com.mmorpg.mir.model.country.packet;

public class SM_Country_UpgradeFlag {
	private String flagId;

	public static SM_Country_UpgradeFlag valueOf(String doorId) {
		SM_Country_UpgradeFlag door = new SM_Country_UpgradeFlag();
		door.flagId = doorId;
		return door;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

}
