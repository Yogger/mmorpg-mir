package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Group_Member_Position {

	private long playerId;
	private int mapId;
	private int x;
	private int y;
	private int instanceId;
	
	public static SM_Group_Member_Position valueOf(Player player) {
		SM_Group_Member_Position s = new SM_Group_Member_Position();
		s.playerId = player.getObjectId();
		s.x = player.getX();
		s.y = player.getY();
		s.mapId = player.getMapId();
		s.instanceId = player.getInstanceId();
		return s;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
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
