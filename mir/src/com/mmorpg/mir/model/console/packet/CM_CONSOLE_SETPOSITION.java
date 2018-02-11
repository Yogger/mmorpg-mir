package com.mmorpg.mir.model.console.packet;

public class CM_CONSOLE_SETPOSITION {
	private int worldId;
	private int x;
	private int y;

	public int getWorldId() {
		return worldId;
	}

	public void setWorldId(int worldId) {
		this.worldId = worldId;
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
