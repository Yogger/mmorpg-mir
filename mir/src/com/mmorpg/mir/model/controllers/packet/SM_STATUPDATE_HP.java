package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Creature;

public class SM_STATUPDATE_HP {
	private long currentHp;
	private long maxHp;

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

	public static SM_STATUPDATE_HP valueOf(Creature creature) {
		SM_STATUPDATE_HP result = new SM_STATUPDATE_HP();
		result.currentHp = creature.getLifeStats().getCurrentHp();
		result.maxHp = creature.getLifeStats().getMaxHp();
		return result;
	}

}
