package com.mmorpg.mir.model.world.packet;

public class SM_Update_Position {
	private int mapId;
	private int x;
	private int y;
	private int instance;

	public static SM_Update_Position valueOf(int mapId, int x, int y, int instance) {
		SM_Update_Position sm = new SM_Update_Position();
		sm.mapId = mapId;
		sm.x = x;
		sm.y = y;
		sm.instance = instance;
		return sm;
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

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

}
