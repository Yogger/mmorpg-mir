package com.mmorpg.mir.model.country.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.packet.SM_Country_UpgradeDoor;

/**
 * 国家科技
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-15
 * 
 */
public class Technology {
	public static final String DOOR_OBJECTID = "33333";
	private static final String INIT_DOORID = "1";
	private Country country;
	/** 当前城门 */
	private String doorId;
	/** 军工厂 */
	private ArmsFactory armsFactory;

	public static Technology valueOf() {
		Technology technology = new Technology();
		technology.doorId = INIT_DOORID;
		technology.armsFactory = ArmsFactory.valueOf();
		return technology;
	}

	@JsonIgnore
	public void upgradeDoor(String nextDoorId) {
		this.doorId = nextDoorId;
		country.sendPacket(SM_Country_UpgradeDoor.valueOf(doorId));
	}

	public String getDoorId() {
		return doorId;
	}

	public void setDoorId(String doorId) {
		this.doorId = doorId;
	}

	public ArmsFactory getArmsFactory() {
		return armsFactory;
	}

	public void setArmsFactory(ArmsFactory armsFactory) {
		this.armsFactory = armsFactory;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
		this.getArmsFactory().setTechnology(this);
	}

}
