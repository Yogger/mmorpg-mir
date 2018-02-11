package com.mmorpg.mir.model.controllers.stats;

import com.mmorpg.mir.model.controllers.packet.SM_ATTACK_STATUS;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class LorryLifeStats extends CreatureLifeStats<Lorry> {

	public LorryLifeStats(Creature owner, long currentHp, long currentMp, boolean die) {
		super(owner, currentHp, currentMp);
		this.alreadyDead = die;
	}

	@Override
	public void sendHpDamage(Creature attacker, int skillId) {
		owner.addPacketBroadcastMask(BroadcastMode.BORADCAST_DAMAGE_STAT);
		if (getOwner() instanceof Lorry) {
			PacketSendUtility.sendPacket(((Lorry) getOwner()).getOwner(), SM_ATTACK_STATUS.valueOf(getOwner()));
		}
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
