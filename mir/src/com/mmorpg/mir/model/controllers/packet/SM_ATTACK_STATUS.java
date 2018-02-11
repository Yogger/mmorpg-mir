package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_ATTACK_STATUS {

	private long objId;
	private long currentHp;
	private long currentBarrier;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public static SM_ATTACK_STATUS valueOf(Creature creature) {
		SM_ATTACK_STATUS result = new SM_ATTACK_STATUS();
		result.objId = creature.getObjectId();
		result.currentHp = creature.getLifeStats().getCurrentHp();
		result.currentBarrier = creature.getLifeStats().getCurrentBarrier();
		return result;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

}
