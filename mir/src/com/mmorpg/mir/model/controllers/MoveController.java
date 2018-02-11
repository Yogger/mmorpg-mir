package com.mmorpg.mir.model.controllers;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.packet.SM_Move;

/**
 * @author ATracer
 * 
 */
public final class MoveController {

	private static final Logger logger = Logger.getLogger(MoveController.class);
	private Future<?> moveTask;
	private Creature owner;

	private Road road;

	private AtomicBoolean isStopped = new AtomicBoolean(Boolean.TRUE);

	private int moveCounter;

	private final int step;

	private boolean needUpdate;

	private long preStemTime;

	private double cost = 0;

	private int updateX;

	private int updateY;

	private boolean isFollowTarget;

	private long lastUpdatePositionTime;

	private int updatePositionCount;

	/**
	 * 
	 * @param owner
	 */
	public MoveController(Creature owner) {
		this.owner = owner;
		this.step = owner.getMoveUpdateSensitivity();
		if (owner.getPosition() != null) {
			// 当玩家第一次构建Player时，没有生成Position对象
			this.updateX = owner.getX();
			this.updateY = owner.getY();
		}
	}

	public void setNewRoads(int x, int y, Road road) {
		if (x != owner.getX() || y != owner.getY()) {
			if (owner.isObjectType(ObjectType.PLAYER)) {
				int range = PlayerManager.getInstance().CLIENT_SAFERANGE.getValue();
				boolean unSafeRange = !MathUtil.isInRange(owner.getX(), owner.getY(), x, y, range, range);
				if (!unSafeRange) {
					if (owner.getKnownList().knownObjectType(ObjectType.SUPERVISOR)) {
						int range1 = PlayerManager.getInstance().CLIENT_SAFERANGE_SUPERVISOR.getValue();
						unSafeRange = !MathUtil.isInRange(owner.getX(), owner.getY(), x, y, range1, range1);
					}
				}
				if (unSafeRange) {
					((Player) owner).sendUpdatePosition();
					owner.getMoveController().stopMoving();
					return;
				}
				if ((System.currentTimeMillis() - lastUpdatePositionTime) < PlayerManager.getInstance().CLIENT_UPDATE_POSITION_TIME
						.getValue()) {
					updatePositionCount++;
					if (updatePositionCount > PlayerManager.getInstance().CLIENT_UPDATE_POSITION_COUNT.getValue()) {
						// SessionManager.getInstance().kick(owner.getObjectId());
						// logger.error(String.format("玩家[%s]移动违章", ((Player)
						// owner).getName()));
						LogManager.moveUpdate((Player) owner);
						return;
					}
				} else {
					this.updatePositionCount = 0;
					this.lastUpdatePositionTime = System.currentTimeMillis();
				}
			}
			World.getInstance().updatePosition(owner, x, y, (byte) 1, Boolean.FALSE);
		}
		if (road != null && (!road.isOver())) {
			PacketSendUtility.broadcastPacket(owner, SM_Move.valueOf(owner, x, y, road.getLeftRoads(), (byte) 1));
			synchronized (this) {
				preStemTime = System.currentTimeMillis();
				cost = 0;
				this.road = road;
				isStopped.set(Boolean.FALSE);
			}
		} else {
			stopMoving();
		}
	}

	public byte[] getLeftRoads() {
		return isStopped.get() ? null : road.getLeftRoads();
	}

	private boolean isScheduled() {
		return moveTask != null && !moveTask.isCancelled();
	}

	public synchronized void schedule() {
		if (isScheduled())
			return;

		moveTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					move();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("move error", e);
				}
			}
		}, 0, 300);
	}

	private void move() {
		if (isStopped.get()) {
			return;
		}
		if (!owner.canPerformMove() || owner.isCasting()) {
			stopMoving();
			return;
		}

		doMove();

		if (needUpdate && (++moveCounter % step == 0)) {
			needUpdate = Boolean.FALSE;
			owner.updateKnownlist();
		}
	}

	private synchronized void doMove() {
		if (!isStopped.get()) {
			long speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED);

			int ownerX = owner.getX();
			int ownerY = owner.getY();

			double need = preStemTime + cost - System.currentTimeMillis();

			byte dir = owner.getHeading();

			while (need <= 0 && (!isStopped.get())) {
				dir = road.poll();
				DirectionEnum direction = DirectionEnum.values()[dir];

				// double use = direction.getLength() * 1000.0D / (speed - 10 >
				// 0 ? speed - 10 : 1);
				double use = direction.getLength() * 1000.0D / speed;
				need += use;
				ownerX += direction.getAddX();
				ownerY += direction.getAddY();

				WorldMap worldMap = owner.getPosition().getMapRegion().getParent().getParent();
				if (worldMap.isOut(ownerX, ownerY) || worldMap.isBlock(ownerX, ownerY)) {
					stopMoving();
					return;
				}

				cost = need;
				if (cost > 0) {
					preStemTime = System.currentTimeMillis();
				}

				if (road.isOver()) {
					isStopped.set(Boolean.TRUE);
					owner.getController().onStopMove();
					break;
				}
			}

			World.getInstance().updatePosition(owner, ownerX, ownerY, dir, Boolean.FALSE);

			if (isFollowTarget) {
				VisibleObject target = owner.getTarget();
				if (target != null) {
					int dis = MathUtil.getGridDistance(ownerX, ownerY, target.getX(), target.getY());
					if (dis <= owner.getAtkRange()) {
						stopMoving();
					}
				}
			}

			switch (owner.getObjectType()) {
			case MONSTER:
			case ROBOT:
				if (MathUtil.getGridDistance(ownerX, ownerY, updateX, updateY) > 10) {
					updateX = ownerX;
					updateY = ownerY;
					needUpdate = Boolean.TRUE;
				}
				break;
			default:
				needUpdate = Boolean.TRUE;
				break;
			}
		}
	}

	public boolean isStopped() {
		return isStopped.get();
	}

	public boolean isFollowTarget() {
		return isFollowTarget;
	}

	public void setFollowTarget(boolean isFollowTarget) {
		this.isFollowTarget = isFollowTarget;
	}

	public void stopMoving() {

		if (isStopped.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
			owner.getController().stopMoving();
			owner.getController().onStopMove();
		}

	}

	public void stop() {

		stopMoving();

		if (moveTask != null) {
			if (!moveTask.isCancelled())
				moveTask.cancel(Boolean.TRUE);
			moveTask = null;
		}
	}
}
