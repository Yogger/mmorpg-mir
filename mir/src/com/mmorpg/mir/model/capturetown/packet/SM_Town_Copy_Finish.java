package com.mmorpg.mir.model.capturetown.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Town_Copy_Finish {

	private int type;

	private String ownerName;

	private int countryValue;

	private int robFeats;
	
	private String key;

	public static SM_Town_Copy_Finish valueOf(int t, Player targetPlayer, int robFeats, String key) {
		SM_Town_Copy_Finish sm = new SM_Town_Copy_Finish();
		sm.type = t;
		sm.ownerName = targetPlayer.getName();
		sm.countryValue = targetPlayer.getCountryValue();
		sm.robFeats = robFeats;
		sm.key = key;
		return sm;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public int getRobFeats() {
		return robFeats;
	}

	public void setRobFeats(int robFeats) {
		this.robFeats = robFeats;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}