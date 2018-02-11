package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.gameobjects.Lorry;

public class SM_Lorry_MapInfo {

	private int mapId;
	private int x;
	private int y;

	public static SM_Lorry_MapInfo valueOf(Lorry lorry) {
		SM_Lorry_MapInfo sm = new SM_Lorry_MapInfo();
		sm.mapId = lorry.getMapId();
		sm.x = lorry.getX();
		sm.y = lorry.getY();
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
}
