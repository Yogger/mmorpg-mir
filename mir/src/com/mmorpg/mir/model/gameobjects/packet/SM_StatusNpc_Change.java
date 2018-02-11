package com.mmorpg.mir.model.gameobjects.packet;

import com.mmorpg.mir.model.gameobjects.StatusNpc;

public class SM_StatusNpc_Change {
	private long objectId;
	private int status;

	public static SM_StatusNpc_Change valueOf(StatusNpc statusNpc) {
		SM_StatusNpc_Change sm = new SM_StatusNpc_Change();
		sm.objectId = statusNpc.getObjectId();
		sm.status = statusNpc.getStatus();
		return sm;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
