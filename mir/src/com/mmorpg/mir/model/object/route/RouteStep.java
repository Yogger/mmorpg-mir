package com.mmorpg.mir.model.object.route;

public class RouteStep {

	private int mapId;
	private int x;
	private int y;

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

	public static RouteStep valueOf(int mapId, int x, int y) {
		RouteStep step = new RouteStep();
		step.mapId = mapId;
		step.x = x;
		step.y = y;
		return step;
	}
}
