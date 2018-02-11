package com.mmorpg.mir.model.world.packet;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.utils.MathUtil;

public class SM_Move {
	private long objId;
	private short x;
	private short y;
	private byte[] roads;
	private byte moveType;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public byte[] getRoads() {
		return roads;
	}

	public void setRoads(byte[] roads) {
		this.roads = roads;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public byte getMoveType() {
		return moveType;
	}

	public void setMoveType(byte moveType) {
		this.moveType = moveType;
	}

	public static SM_Move valueOf(Creature creature, int x, int y, byte[] roads, byte moveType) {
		SM_Move result = new SM_Move();
		result.x = (short) x;
		result.y = (short) y;
		result.objId = creature.getObjectId();
		result.roads = MathUtil.zipRoads(roads);
		result.moveType = moveType;
		return result;
	}
}
