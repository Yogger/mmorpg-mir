package com.mmorpg.mir.model.controllers.stats;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;

public class MonsterLifeStats extends CreatureLifeStats<Monster> {

	public MonsterLifeStats(Creature owner, long currentHp, long currentMp, boolean die) {
		super(owner, currentHp, currentMp);
		this.alreadyDead = die;
	}

	@Override
	protected void onIncreaseMp(long value) {
	}

	@Override
	protected void onReduceMp() {
	}

	@Override
	protected void onIncreaseHp(long value) {
	}

	@Override
	protected void onReduceHp() {
	}

}
