package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.MoveDesire;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.object.creater.ServantCreater;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_Move;

public class FollowTargetDesire extends AbstractDesire implements MoveDesire {
	private Summon owner;

	/**
	 * @param crt
	 * @param desirePower
	 */
	public FollowTargetDesire(Summon owner, int desirePower) {
		super(desirePower);
		this.owner = owner;
	}

	@Override
	public boolean handleDesire(AI ai) {
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;

		Player target = owner.getMaster();

		Position newPosition = ServantCreater.getCanUsePosition(World.getInstance(), target);
		if (target.getMapId() != owner.getMapId() || target.getInstanceId() != owner.getInstanceId()) {
			World.getInstance().despawn(owner);
			World.getInstance().setPosition(owner, target.getMapId(), target.getInstanceId(), newPosition.getX(),
					newPosition.getY(), owner.getHeading());
			World.getInstance().spawn(owner);
			return true;
		}

		int fx = owner.getX();
		int fy = owner.getY();
		int tx = target.getX();
		int ty = target.getY();

		if (target.getMoveController().isStopped()) {
			// TODO 这里计算跟随距离
			int distance = MathUtil.getGridDistance(fx, fy, tx, ty);
			if (distance <= 5) {
				owner.getMoveController().stopMoving();
				ai.setAiState(AIState.THINKING);
				return true;
			}
		}

		if (!owner.getKnownList().knowns(target)
				|| !MathUtil.isInRange(owner, target,
						BeautyGirlConfig.getInstance().SERVANT_TRANSPORT_DISTANCE.getValue(),
						BeautyGirlConfig.getInstance().SERVANT_TRANSPORT_DISTANCE.getValue())) {
			World.getInstance().updatePosition(owner, newPosition.getX(), newPosition.getY(), owner.getHeading());
			PacketSendUtility
					.broadcastPacket(owner, SM_Move.valueOf(owner, owner.getX(), owner.getY(), null, (byte) 0));
			return true;
		}

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		if (!owner.canPerformMove()) {
			return true;
		}

		Road road = MathUtil.SmoothFindRoad(owner.getMapId(), fx, fy, newPosition.getX(), newPosition.getY());

		// TODO 这里设置跟随
		if (road != null) {
			owner.getMoveController().setNewRoads(fx, fy, road);
			owner.getMoveController().schedule();
		} else {
			// 如果无法到达，就直接传送
			World.getInstance().updatePosition(owner, newPosition.getX(), newPosition.getY(), owner.getHeading());
		}

		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FollowTargetDesire))
			return false;

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