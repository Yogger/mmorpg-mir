package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.object.route.RouteRoad;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_Move;

public class AggressionRouteWalkDesire extends AbstractDesire implements MoveDesire {
	private Npc owner;

	public AggressionRouteWalkDesire(Npc npc, int power) {
		super(power);
		owner = npc;
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null)
			return false;

		if (owner.findEnemy())
			return false;

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		boolean go = true;

		while (go) {
			RouteRoad routeRoad = owner.getRouteRoad();

			if (routeRoad.isOver()) {
				ai.handleEvent(Event.ROUTE_OVER);
				return false;
			}

			RouteStep step = routeRoad.getNextStep();

			int fx = owner.getX();
			int fy = owner.getY();
			int mapId = owner.getMapId();

			if (step.getMapId() != mapId) {
				// 如果跨越地图了，就直接传送
				World.getInstance().despawn(owner);
				World.getInstance().setPosition(owner, step.getMapId(), World.INIT_INSTANCE, step.getX(), step.getY(),
						owner.getHeading());
				World.getInstance().spawn(owner);
			}

			if (fx == step.getX() && fy == step.getY()) {
				routeRoad.overStep();
				continue;
			}

			go = false;

			Road road = MathUtil.SmoothFindRoad(owner.getMapId(), fx, fy, step.getX(), step.getY());

			if (road == null) {
				road = MathUtil.findRoadByStep(owner.getMapId(), fx, fy, step.getX(), step.getY(), 2000);
			}

			if (road != null) {
				owner.getMoveController().setNewRoads(owner.getX(), owner.getY(), road);
				owner.getMoveController().schedule();
			} else {
				// 如果无法到达，就直接传送
				World.getInstance().updatePosition(owner, step.getX(), step.getY(), owner.getHeading());
				PacketSendUtility.broadcastPacket(owner,
						SM_Move.valueOf(owner, owner.getX(), owner.getY(), null, (byte) 0));
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
