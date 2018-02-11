package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.gameobjects.Npc;

public class SM_TechCopy_Status {
	/** BOSS当前血量 **/
	private long currentHp;
	/** BOSS血量上限 **/
	private long maxHp;
	
	public static SM_TechCopy_Status valueOf(Npc npc) {
		SM_TechCopy_Status sm = new SM_TechCopy_Status();
		sm.currentHp = npc.getLifeStats().getCurrentHp();
		sm.maxHp = npc.getLifeStats().getMaxHp();
		return sm;
	}

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

}
