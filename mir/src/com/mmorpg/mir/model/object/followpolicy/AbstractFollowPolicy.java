package com.mmorpg.mir.model.object.followpolicy;

import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.utils.MathUtil;

public abstract class AbstractFollowPolicy implements IFollowPolicy {

	protected final Npc owner;

	protected AbstractFollowPolicy(Npc owner) {
		this.owner = owner;
	}

	public Npc getOwner() {
		return owner;
	}

	@Override
	public boolean outWarning(int tx, int ty) {
		int warnrange = owner.getWarnrange();
		int[] bornXY = getBornXY();

		int distance = MathUtil.getGridDistance(tx, ty, bornXY[0], bornXY[1]);
		if (distance > warnrange) {
			return true;
		}

		return false;
	}

	@Override
	public boolean tooFarFromHome(int tx, int ty) {
		int homeRange = owner.getHomeRange();
		int[] bornXY = getBornXY();

		int distance1 = MathUtil.getGridDistance(owner.getX(), owner.getY(), bornXY[0], bornXY[1]);
		int distance2 = MathUtil.getGridDistance(tx, ty, bornXY[0], bornXY[1]);
		if (distance1 > homeRange || distance2 > homeRange) {
			return true;
		}

		return false;
	}

	protected abstract int[] getBornXY();
}
