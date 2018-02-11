package com.mmorpg.mir.model.controllers.stats;

import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;

public class NpcLifeStats extends CreatureLifeStats<Npc> {
	/**
	 * 
	 * @param owner
	 */
	public NpcLifeStats(Npc owner) {
		super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
				StatEnum.MAXMP));
	}

	@Override
	protected void onIncreaseHp(long value) {

	}

	@Override
	protected void onIncreaseMp(long value) {
		// nothing todo
	}

	@Override
	protected void onReduceHp() {
		// nothing todo
	}

	@Override
	protected void onReduceMp() {
		// nothing todo
	}

	@Override
	protected void triggerRestoreTask() {

	}
}
