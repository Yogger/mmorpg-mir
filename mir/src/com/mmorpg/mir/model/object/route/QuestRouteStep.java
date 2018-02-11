package com.mmorpg.mir.model.object.route;

import java.util.ArrayList;
import java.util.List;

import com.mmorpg.mir.model.ai.desires.Desire;
import com.mmorpg.mir.model.ai.desires.impl.FindAndAttackDesire;
import com.mmorpg.mir.model.ai.desires.impl.MoveToTargetDesire;
import com.mmorpg.mir.model.gameobjects.BigBrother;

public class QuestRouteStep {

	private String questId;
	private RouteStep[] routeSteps;

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public RouteStep[] getRouteSteps() {
		return routeSteps;
	}

	public void setRouteSteps(RouteStep[] routeSteps) {
		this.routeSteps = routeSteps;
	}

	public List<Desire> createDesire(BigBrother summon) {
		List<Desire> result = new ArrayList<Desire>(2);
		MoveToTargetDesire desire = new MoveToTargetDesire(summon, 1, questId, new RouteRoad(routeSteps));
		FindAndAttackDesire faDesire = new FindAndAttackDesire(summon, questId, 1);
		result.add(desire);
		result.add(faDesire);
		return result;
	}
}
