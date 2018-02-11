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

public class RouteWalkDesire extends AbstractDesire implements MoveDesire {
	private Npc owner;

	public RouteWalkDesire(Npc npc, int power) {
		super(power);
		owner = npc;
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null)
			return false;

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		RouteRoad routeRoad = owner.getRouteRoad();

		if (routeRoad.isOver()) {
			ai.handleEvent(Event.ROUTE_OVER);
			return false;
		}

		if (ai.getOwner() instanceof Npc) {
			if (!((Npc) ai.getOwner()).canMove()) {
				return false;
			}
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
			return true;
		}

		if (MathUtil.isInRange(fx, fy, step.getX(), step.getY(), 3, 3)) {
			routeRoad.overStep();
			return true;
		}

		Road road = MathUtil.SmoothFindRoad(mapId, fx, fy, step.getX(), step.getY());

		if (road == null) {
			road = MathUtil.findRoadByStep(mapId, fx, fy, step.getX(), step.getY(), 5000);
		}

		if (road != null) {
			owner.getMoveController().setNewRoads(fx, fy, road);
			owner.getMoveController().schedule();
		} else {
			// 如果无法到达，就直接传送
			World.getInstance().updatePosition(owner, step.getX(), step.getY(), owner.getHeading());
			PacketSendUtility
					.broadcastPacket(owner, SM_Move.valueOf(owner, owner.getX(), owner.getY(), null, (byte) 0));
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
