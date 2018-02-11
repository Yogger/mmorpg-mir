package com.mmorpg.mir.model.controllers.stats;

public class RelivePosition {
	private int x;
	private int y;
	private int mapId;

	public static RelivePosition valueOf(int x, int y, int map) {
		RelivePosition p = new RelivePosition();
		p.setX(x);
		p.setY(y);
		p.setMapId(map);
		return p;
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

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

}
