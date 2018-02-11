package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_STATUPDATE_BARRIER {
	private long currentBarrier;
	private long objectId;
	private long maxBarrier;

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public long getMaxBarrier() {
		return maxBarrier;
	}

	public void setMaxBarrier(long maxBarrier) {
		this.maxBarrier = maxBarrier;
	}

	public static SM_STATUPDATE_BARRIER valueOf(Creature creature) {
		SM_STATUPDATE_BARRIER result = new SM_STATUPDATE_BARRIER();
		result.currentBarrier = creature.getLifeStats().getCurrentBarrier();
		result.maxBarrier = creature.getLifeStats().getMaxBarrier();
		result.objectId = creature.getObjectId();
		return result;
	}

}
