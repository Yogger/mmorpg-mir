package com.mmorpg.mir.model.country.packet;

public class SM_ReserveKing_CallTogether {
	private String name;

	private int mapId;

	private int x;

	private int y;

	private int availableCount;
	
	private long time;

	public static SM_ReserveKing_CallTogether valueOf(String name, int mapId, int x, int y, int availableCount) {
		SM_ReserveKing_CallTogether result = new SM_ReserveKing_CallTogether();
		result.name = name;
		result.mapId = mapId;
		result.x = x;
		result.y = y;
		result.availableCount = availableCount;
		result.time = System.currentTimeMillis();
		return result;
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
