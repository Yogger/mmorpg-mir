package com.mmorpg.mir.model.capturetown.packet;

public class SM_Get_Specified_Town_Info {

	private String key;
	
	private long ownerId;
	
	private int countryValue;
	
	private String ownerName;
	
	private long accFeats;
	
	public static SM_Get_Specified_Town_Info valueOf(String id, String name, long playerId, int country, long feats) {
		SM_Get_Specified_Town_Info sm = new SM_Get_Specified_Town_Info();
		sm.key = id;
		sm.ownerId = playerId;
		sm.countryValue = country;
		sm.accFeats = feats;
		sm.ownerName = name;
		return sm;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public long getAccFeats() {
		return accFeats;
	}

	public void setAccFeats(long accFeats) {
		this.accFeats = accFeats;
	}
	
}
