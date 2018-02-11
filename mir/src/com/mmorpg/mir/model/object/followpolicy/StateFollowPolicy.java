package com.mmorpg.mir.model.object.followpolicy;

import com.mmorpg.mir.model.gameobjects.Npc;

public class StateFollowPolicy extends AbstractFollowPolicy {

	public StateFollowPolicy(Npc owner) {
		super(owner);
	}

	@Override
	protected int[] getBornXY() {
		return new int[] { owner.getBornX(), owner.getBornY() };
	}
}
