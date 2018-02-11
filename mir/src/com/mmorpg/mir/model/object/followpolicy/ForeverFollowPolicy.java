package com.mmorpg.mir.model.object.followpolicy;

import com.mmorpg.mir.model.gameobjects.Npc;

public class ForeverFollowPolicy extends AbstractFollowPolicy {

	public ForeverFollowPolicy(Npc owner) {
		super(owner);
	}

	@Override
	public boolean tooFarFromHome(int tx, int ty) {
		return false;
	}

	@Override
	protected int[] getBornXY() {
		return new int[] { owner.getBornX(), owner.getBornY() };
	}

}
