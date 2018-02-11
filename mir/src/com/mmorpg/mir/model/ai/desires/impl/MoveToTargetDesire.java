package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.object.route.RouteRoad;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.utils.MathUtil;

/**
 * @author san
 * 
 */
public class MoveToTargetDesire extends AbstractDesire implements MoveDesire {
	private BigBrother owner;
	private String questId;
	private RouteRoad road;

	public MoveToTargetDesire(BigBrother owner, int desirePower, String questId, RouteRoad road) {
		super(desirePower);
		this.owner = owner;
		this.questId = questId;
		this.road = road;
	}

	@Override
	public boolean handleDesire(AI ai) {

		Player master = owner.getMaster();
		if (master.getQuestPool().getCompletionHistory().containsKey(questId)) {
			return false;
		}

		// owner.ride();

		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		if (!owner.canPerformMove()) {
			return true;
		}

		int fx = owner.getX();
		int fy = owner.getY();

		if (road.isOver()) {
			return false;
		}

		RouteStep step = road.getNextStep();

		if (fx == step.getX() && fy == step.getY()) {
			road.overStep();
			if (road.isOver()) {
				return false;
			}
		}

		Road road = MathUtil.SmoothFindRoad(owner.getMapId(), fx, fy, step.getX(), step.getY());
		if (road == null) {
			road = MathUtil.findRoad(owner.getMapId(), fx, fy, step.getX(), step.getY());
		}

		owner.getMoveController().setFollowTarget(false);
		owner.getMoveController().setNewRoads(fx, fy, road);
		owner.getMoveController().schedule();

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
