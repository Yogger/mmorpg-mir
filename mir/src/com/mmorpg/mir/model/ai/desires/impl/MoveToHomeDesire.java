package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.controllers.NpcController;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_Heading;
import com.mmorpg.mir.model.world.packet.SM_Move;

/**
 * @author ATracer
 * 
 */
public class MoveToHomeDesire extends AbstractDesire implements MoveDesire {
	private Npc owner;

	public MoveToHomeDesire(Npc owner, int desirePower) {
		super(desirePower);
		this.owner = owner;
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		if (!owner.canPerformMove()) {
			return true;
		}

		if (owner.isAtSpawnLocation()) {
			if (owner.getHeading() != owner.getBornHeading()) {
				if (owner.getObjectResource().isFightOffHeading()) {
					owner.setHeading(owner.getBornHeading());
					PacketSendUtility.broadcastPacket(owner, SM_Heading.valueOf(owner));
				}
			}
			if (owner.getController() instanceof NpcController) {
				((NpcController) owner.getController()).onAtSpawnLocation();
			}
			owner.onAtSpawnLocation();
			ai.handleEvent(Event.BACK_HOME);
			return false;
		}
		int fx = owner.getX();
		int fy = owner.getY();

		Road road = MathUtil.SmoothFindRoad(owner.getMapId(), fx, fy, owner.getBornX(), owner.getBornY());
		if (road == null) {
			road = MathUtil.findRoad(owner.getMapId(), fx, fy, owner.getBornX(), owner.getBornY());
		}

		if (road == null) {
			// 如果找不到回家的路径，为了防止卡bug，这里可以强行拖回
			World.getInstance().updatePosition(owner, owner.getBornX(), owner.getBornY(), owner.getHeading());
			PacketSendUtility
					.broadcastPacket(owner, SM_Move.valueOf(owner, owner.getX(), owner.getY(), null, (byte) 0));
			return true;
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
