package com.mmorpg.mir.model.country.packet;

public class SM_Country_UpgradeFactory {
	private String factoryId;

	public static SM_Country_UpgradeFactory valueOf(String doorId) {
		SM_Country_UpgradeFactory door = new SM_Country_UpgradeFactory();
		door.setFactoryId(doorId);
		return door;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

}
