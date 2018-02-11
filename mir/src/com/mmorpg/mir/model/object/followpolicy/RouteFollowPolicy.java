package com.mmorpg.mir.model.object.followpolicy;

import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.object.route.RouteRoad;

public class RouteFollowPolicy extends AbstractFollowPolicy {

	public RouteFollowPolicy(Npc owner) {
		super(owner);
	}

	@Override
	protected int[] getBornXY() {
		RouteRoad road = owner.getRouteRoad();
		int bornX = owner.getBornX();
		int bornY = owner.getBornY();
		if (!road.isOver()) {
			bornX = road.getNextStep().getX();
			bornY = road.getNextStep().getY();
		}
		return new int[] { bornX, bornY };
	}
}
