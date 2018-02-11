package com.mmorpg.mir.model.gang.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Gang_Ask_Help {
	private String name;
	private int mapId;
	private int x;
	private int y;
	private long time;

	public static SM_Gang_Ask_Help valueOf(Player inviter, long t) {
		SM_Gang_Ask_Help sm = new SM_Gang_Ask_Help();
		sm.name = inviter.getName();
		sm.mapId = inviter.getMapId();
		sm.x = inviter.getX();
		sm.y = inviter.getY();
		sm.time = t;
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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
