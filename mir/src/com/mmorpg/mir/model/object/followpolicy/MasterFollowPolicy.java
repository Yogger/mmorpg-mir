package com.mmorpg.mir.model.object.followpolicy;

import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;

public class MasterFollowPolicy extends AbstractFollowPolicy {

	public MasterFollowPolicy(Npc owner) {
		super(owner);
	}

	@Override
	protected int[] getBornXY() {
		Summon summon = (Summon) owner;
		Player master = summon.getMaster();
		return new int[] { master.getX(), master.getY() };
	}
}
