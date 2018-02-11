package com.mmorpg.mir.model.gameobjects.packet;

public class SM_CreatureSpeedChange {
	private long speed;
	private long objectId;

	public static SM_CreatureSpeedChange valueOf(long speed, long objectId) {
		SM_CreatureSpeedChange csc = new SM_CreatureSpeedChange();
		csc.speed = speed;
		csc.objectId = objectId;
		return csc;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

}
