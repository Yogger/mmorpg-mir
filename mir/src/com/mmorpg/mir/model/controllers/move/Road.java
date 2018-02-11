package com.mmorpg.mir.model.controllers.move;

public class Road {

	private final byte[] roads;
	private final int totalLength;
	private int index = 0;

	private Road(byte[] roads) {
		if (roads == null) {
			this.roads = new byte[] {};
			this.totalLength = 0;
		} else {
			this.roads = roads;
			this.totalLength = roads.length;
		}
	}

	public boolean isOver() {
		return index >= totalLength;
	}

	public byte[] getLeftRoads() {
		byte[] result = null;
		int length = totalLength - index;
		if (length > 0) {
			result = new byte[length];
			System.arraycopy(getRoads(), index, result, 0, length);
		}
		return result;
	}

	public static Road valueOf(byte... roads) {
		Road road = new Road(roads);
		return road;
	}

	public byte poll() {
		return roads[index++];
	}

	public byte[] getRoads() {
		return roads;
	}
}
