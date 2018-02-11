package com.mmorpg.mir.model.ai.desires.impl;

import java.util.Random;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.utils.MathUtil;

public class WalkDesire extends AbstractDesire implements MoveDesire {
	private Npc owner;

	private int mod;

	public WalkDesire(Npc npc, int power) {
		super(power);
		owner = npc;
		mod = getExecutioinMod(1);
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null)
			return false;

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		if (!owner.canPerformMove()) {
			return true;
		}

		int fx = owner.getX();
		int fy = owner.getY();

		int step = 3;

		Random random = MathUtil.getRandom();
		int tx = owner.getBornX() + random.nextInt(step * 2) - step;
		int ty = owner.getBornY() + random.nextInt(step * 2) - step;

		if (tx != fx || ty != fy) {
			Road road = MathUtil.SmoothFindRoad(owner.getMapId(), owner.getX(), owner.getY(), tx, ty);
			if (road != null) {
				owner.getMoveController().setNewRoads(owner.getX(), owner.getY(), road);
				owner.getMoveController().schedule();
				executionCounter = 0;
				mod = getExecutioinMod(1);
			}
		}

		return true;
	}

	@Override
	public int getExecutionInterval() {
		return mod;
	}

	@Override
	public void onClear() {
		owner.getMoveController().stop();
	}

	private int getExecutioinMod(int multy) {
		Random random = MathUtil.getRandom();
		return (random.nextInt(4) + 4) * multy;
	}
}
