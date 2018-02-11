package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_Relive {
	private long objId;
	private long currentHp;
	private long currentMp;

	public static SM_Relive valueOf(Creature owner, long currentHp, long currentMp) {
		SM_Relive result = new SM_Relive();
		result.objId = owner.getObjectId();
		result.setCurrentHp(currentHp);
		result.setCurrentMp(currentMp);
		return result;
	}

	public long getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(long currentMp) {
		this.currentMp = currentMp;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}
}
