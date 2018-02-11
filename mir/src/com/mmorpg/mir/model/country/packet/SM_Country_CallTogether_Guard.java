package com.mmorpg.mir.model.country.packet;

public class SM_Country_CallTogether_Guard {
	
	private String name;

	private int mapId;
	
	private int x;
	
	private int y;
	
	private int availableCount;
	
	private long time;
	
	public static SM_Country_CallTogether_Guard valueOf(String name, int mapId, int x, int y, int count) {
		SM_Country_CallTogether_Guard sm = new SM_Country_CallTogether_Guard();
		sm.name = name;
		sm.mapId = mapId;
		sm.x = x;
		sm.y = y;
		sm.availableCount = count;
		sm.time = System.currentTimeMillis();
		return sm;
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

}
