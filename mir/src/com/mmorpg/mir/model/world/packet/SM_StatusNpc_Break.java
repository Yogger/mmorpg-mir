package com.mmorpg.mir.model.world.packet;

public class SM_StatusNpc_Break {
	private long objectId;

	public static SM_StatusNpc_Break valueOf(long objectId) {
		SM_StatusNpc_Break sm = new SM_StatusNpc_Break();
		sm.objectId = objectId;
		return sm;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
}
