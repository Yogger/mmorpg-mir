package com.mmorpg.mir.model.country.packet;

public class SM_Country_CallTogether {

	private byte token;

	private String name;
	
	private int mapId;
	
	private int x;
	
	private int y;
	
	private int availableCount;
	
	private long time;
	
	private byte officials;
	
	public static SM_Country_CallTogether valueOf(byte token, String name, int mapId, int x, int y, int count, byte offi) {
		SM_Country_CallTogether sm = new SM_Country_CallTogether();
		sm.token = token;
		sm.name = name;
		sm.mapId = mapId;
		sm.x = x;
		sm.y = y;
		sm.availableCount = count;
		sm.time = System.currentTimeMillis();
		sm.officials = offi;
		return sm;
	}

	public byte getToken() {
		return token;
	}

	public void setToken(byte token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public byte getOfficials() {
		return officials;
	}

	public void setOfficials(byte officials) {
		this.officials = officials;
	}

}
