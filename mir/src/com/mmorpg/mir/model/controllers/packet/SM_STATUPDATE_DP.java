package com.mmorpg.mir.model.controllers.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_STATUPDATE_DP {
	private long currentDp;

	public long getCurrentDp() {
		return currentDp;
	}

	public void setCurrentDp(long currentDp) {
		this.currentDp = currentDp;
	}

	public static SM_STATUPDATE_DP valueOf(Player creature) {
		SM_STATUPDATE_DP result = new SM_STATUPDATE_DP();
		result.currentDp = creature.getLifeStats().getCurrentDp();
		return result;
	}

}
