package com.mmorpg.mir.model.ai.desires.impl;

import java.util.Random;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.utils.MathUtil;

public class AggressionWalkDesire extends AbstractDesire implements MoveDesire {
	private Npc owner;

	private int mod = 0;

	private int step = 0;

	public AggressionWalkDesire(Npc npc, int power) {
		super(power);
		owner = npc;
		Random random = MathUtil.getRandom();
		step = 5 + random.nextInt(15);
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null)
			return false;

		if (!owner.getMoveController().isStopped())
			return true;

		if (!owner.canPerformMove())
			return true;

		if (owner.findEnemy())
			return true;

		if (++mod % step != 0)
			return true;

		mod = 0;
		Random random = MathUtil.getRandom();
		step = 5 + random.nextInt(15);

		int fx = owner.getX();
		int fy = owner.getY();

		int step = 4;

		int tx = owner.getBornX() + random.nextInt(step * 2) - step;
		int ty = owner.getBornY() + random.nextInt(step * 2) - step;

		if (tx != fx || ty != fy) {
			Road road = MathUtil.SmoothFindRoad(owner.getMapId(), owner.getX(), owner.getY(), tx, ty);
			if (road != null) {
				owner.getMoveController().setNewRoads(owner.getX(), owner.getY(), road);
				owner.getMoveController().schedule();
			}
		}

		return true;
	}

	@Override
	public int getExecutionInterval() {
		return 1;
	}

	@Override
	public void onClear() {
		owner.getMoveController().stop();
	}

}
