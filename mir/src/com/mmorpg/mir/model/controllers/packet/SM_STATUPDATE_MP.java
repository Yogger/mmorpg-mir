package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_STATUPDATE_MP {
	private long currentMp;
	private long maxMp;

	public long getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(long currentMp) {
		this.currentMp = currentMp;
	}

	public long getMaxMp() {
		return maxMp;
	}

	public void setMaxMp(long maxMp) {
		this.maxMp = maxMp;
	}

	public static SM_STATUPDATE_MP valueOf(Creature creature) {
		SM_STATUPDATE_MP result = new SM_STATUPDATE_MP();
		result.currentMp = creature.getLifeStats().getCurrentMp();
		result.maxMp = creature.getLifeStats().getMaxMp();
		return result;
	}

}
