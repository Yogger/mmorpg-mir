package com.mmorpg.mir.model.world.packet;

public class CM_Move {
	private int x;
	private int y;
	private byte[] roads;

	public byte[] getRoads() {
		return roads;
	}

	public void setRoads(byte[] roads) {
		this.roads = roads;
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

}
