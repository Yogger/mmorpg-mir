package com.mmorpg.mir.model.ai.desires.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.Desire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.object.route.QuestRouteStep;
import com.mmorpg.mir.model.spawn.SpawnManager;

public class BigBrotherDesire extends AbstractDesire implements MoveDesire {

	private List<Desire> desires;

	/**
	 * @param crt
	 * @param desirePower
	 */
	public BigBrotherDesire(BigBrother owner, int desirePower) {
		super(desirePower);
		desires = new ArrayList<Desire>(10);

		QuestRouteStep[] steps = SpawnManager.getInstance().getQuestRouteStep(owner.getMaster().getCountryId());

		for (QuestRouteStep step : steps) {
			desires.addAll(step.createDesire(owner));
		}
	}

	@Override
	public boolean handleDesire(AI ai) {

		Iterator<Desire> ite = desires.iterator();
		while (ite.hasNext()) {
			Desire desire = ite.next();
			if (!desire.handleDesire(ai)) {
				desire.onClear();
				ite.remove();
			} else {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BigBrotherDesire))
			return false;

		return true;
	}

	@Override
	public int getExecutionInterval() {
		return 1;
	}

	@Override
	public void onClear() {

	}
}