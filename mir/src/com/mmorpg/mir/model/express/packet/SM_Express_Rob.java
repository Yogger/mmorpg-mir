package com.mmorpg.mir.model.express.packet;

public class SM_Express_Rob {
	private long objectId;

	public static SM_Express_Rob valueOf(long objectId) {
		SM_Express_Rob sm = new SM_Express_Rob();
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
