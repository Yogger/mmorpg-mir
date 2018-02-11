package com.mmorpg.mir.model.operator.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Operator_Trace {
	private int code;
	private int mapId;
	private int x;
	private int y;
	private int instanceId;

	public static SM_Operator_Trace valueOf(Player player, int code) {
		SM_Operator_Trace sm = new SM_Operator_Trace();
		if (player != null && player.isSpawned()) {
			sm.setMapId(player.getMapId());
			sm.setX(player.getX());
			sm.setY(player.getY());
			sm.setInstanceId(player.getInstanceId());
		}
		sm.code = code;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

}
