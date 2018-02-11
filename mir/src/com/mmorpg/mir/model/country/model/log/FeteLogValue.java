package com.mmorpg.mir.model.country.model.log;


public class FeteLogValue{
	private long playerId;
	// 上香类型
	private int type;
	
	private String name;
	
	private String server;
	
	private int honor;
	
	private long timeMills;
	
	private int countryValue;
	
	public static FeteLogValue valueOf(String server, long objId, int type, String name, int honor, long time, int countryValue) {
		FeteLogValue value = new FeteLogValue();
		value.timeMills = time;
		value.honor = honor;
		value.type = type;
		value.playerId = objId;
		value.name = name;
		value.countryValue = countryValue;
		return value;
	}

	public int getType() {
    	return type;
    }

	public void setType(int type) {
    	this.type = type;
    }

	public long getTimeMills() {
    	return timeMills;
    }

	public void setTimeMills(long timeMills) {
    	this.timeMills = timeMills;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public long getPlayerId() {
    	return playerId;
    }

	public void setPlayerId(long playerId) {
    	this.playerId = playerId;
    }

	public String getServer() {
    	return server;
    }

	public void setServer(String server) {
    	this.server = server;
    }

	public int getHonor() {
    	return honor;
    }

	public void setHonor(int honor) {
    	this.honor = honor;
    }

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}
	
}
