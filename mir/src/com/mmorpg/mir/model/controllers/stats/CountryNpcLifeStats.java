package com.mmorpg.mir.model.controllers.stats;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;

public class CountryNpcLifeStats extends MonsterLifeStats {

	public CountryNpcLifeStats(Creature owner, long currentHp, long currentMp, boolean die) {
		super(owner, currentHp, currentMp, die);
	}

	@Override
	public void sendHpDamage(Creature attacker, int skillId) {
		owner.addPacketBroadcastMask(BroadcastMode.BORADCAST_DAMAGE_STAT);
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
