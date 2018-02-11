package com.mmorpg.mir.model.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.controllers.LorryController;
import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.express.packet.SM_Express_Fail;
import com.mmorpg.mir.model.express.packet.SM_Express_Faraway_Close;
import com.mmorpg.mir.model.express.packet.SM_Express_Faraway_Open;
import com.mmorpg.mir.model.express.resource.ExpressResource;
import com.mmorpg.mir.model.express.resource.ExpressResource.LorryColor;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldPosition;
import com.windforce.common.event.core.EventBusManager;

public class Lorry extends Npc {

	/** 是否被劫持 */
	private volatile boolean rob;
	/** 最近被攻击的时间 */
	private long lastOnAttackTime;
	/** 颜色 */
	private int color;
	/** 所属玩家 */
	private Player owner;
	/** 运镖任务 */
	private List<Future<?>> futures = new ArrayList<Future<?>>();
	/** 远离主人失败倒计时 */
	private Future<?> farawayFuture;
	/** 网络延迟宽容时间 */
	private static final int NET_DELAY = 2000;
	/** 最后一次远离时间 */
	private long lastFarawayTime;
	/** 开始时间 */
	private long startTime;
	/** 是否能移动 */
	private boolean canMove = true;
	/** 地图信息 */
	private Future<?> mapInfoFuture;
	/** 是否停止 */
	private volatile boolean stop;
	/** 镖车id */
	private String expressId;

	/**
	 * 玩家远离
	 * 
	 * @param farAwayDelay
	 */
	public void faraway(boolean see) {
		if (stop) {
			return;
		}
		if (see) {
			canMove = true;
			if (farawayFuture != null) {
				PacketSendUtility.sendPacket(owner, new SM_Express_Faraway_Close());
				farawayFuture.cancel(false);
				farawayFuture = null;
				lastFarawayTime = 0;
			}
			this.getAi().handleEvent(Event.SEE_PLAYER); // /看见主人了
		} else {
			if (farawayFuture == null) {
				PacketSendUtility.sendPacket(owner, new SM_Express_Faraway_Open());
				farawayFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						fail();
					}
				}, (ExpressManager.getInstance().EXPRESS_FARAWAY_FIAL_TIME.getValue() + NET_DELAY));
				lastFarawayTime = System.currentTimeMillis();
			}
			canMove = false;
		}
	}

	public void fail() {
		fail(true);
	}

	public void fail(boolean addCount) {
		ExpressManager.getInstance().unRegisterLorry(this);
		// 通知前端
		if (SessionManager.getInstance().isOnline(owner.getObjectId())) {
			PacketSendUtility.sendPacket(owner, new SM_Express_Fail());
			owner.getExpress().setBeenNotifyFail(true);
		} else if (getResource().isAddLorryCount()) {
			owner.getExpress().setBeenNotifyFail(false);
		}
		this.getAi().stop();
		World.getInstance().despawn(this);
		stop();
		if (addCount) {
			getOwner().getExpress().addLorryCompleteHistoryCount();
			EventBusManager.getInstance().submit(ExpressEvent.valueOf(getOwner(), expressId));
			if (getResource().isAddLorryCount()) {
				// 剧情副本的镖车不算次数
				owner.getExpress().addLorryCount();
			}
			if (getOwner() != null) {
				LogManager
						.express(getOwner().getObjectId(), getOwner().getPlayerEnt().getServer(), getOwner()
								.getPlayerEnt().getAccountName(), getOwner().getName(), System.currentTimeMillis(),
								expressId, getOwner().getExpress().getLorryCount(), getOwner().getExpress()
										.getLorryCompleteHistoryCount(), 1);
			}
		}
	}

	public ExpressResource getResource() {
		ExpressResource resource = ExpressManager.getInstance().getExpressResource(expressId);
		return resource;
	}

	public void stop() {
		this.stop = true;
		for (Future<?> future : this.getFutures()) {
			if (!future.isCancelled()) {
				future.cancel(false);
			}
		}
		if (farawayFuture != null && !farawayFuture.isCancelled()) {
			farawayFuture.cancel(false);
		}
		if (mapInfoFuture != null && !mapInfoFuture.isCancelled()) {
			mapInfoFuture.cancel(false);
		}
		owner.getExpress().setSelectLorrys(null);
		owner.getExpress().clearLorry();
		this.getController().delete();
	}

	public Lorry(long objId, LorryController controller, WorldPosition position, Player player, int color) {
		super(objId, controller, position);
		controller.setOwner(this);
		setOwner(player);
		this.color = color;
		setStartTime(System.currentTimeMillis());
	}

	@Override
	public boolean canPerformMove() {
		// 镖车不吃控制技能
		return true;
	}

	@Override
	public int getMoveUpdateSensitivity() {
		return 1;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.LORRY;
	}

	@Override
	public boolean canMove() {
		if (!canMove) {
			return false;
		}
		return super.canMove();
	}

	public boolean isRob() {
		return rob;
	}

	public Future<?> getMapInfoFuture() {
		return mapInfoFuture;
	}

	public void setMapInfoFuture(Future<?> mapInfoFuture) {
		this.mapInfoFuture = mapInfoFuture;
	}

	public void setRob(boolean rob) {
		this.rob = rob;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@Override
	protected boolean isPlayerEnemy(Player player) {
		return getCountryValue() != player.getCountryValue();
	}

	@Override
	public int getCountryValue() {
		if (owner != null) {
			return owner.getCountryValue();
		}
		return super.getCountryValue();
	}

	public List<Future<?>> getFutures() {
		return futures;
	}

	public void setFutures(List<Future<?>> futures) {
		this.futures = futures;
	}

	public Future<?> getFarawayFuture() {
		return farawayFuture;
	}

	public void setFarawayFuture(Future<?> farawayFuture) {
		this.farawayFuture = farawayFuture;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastFarawayTime() {
		return lastFarawayTime;
	}

	public void setLastFarawayTime(long lastFarawayTime) {
		this.lastFarawayTime = lastFarawayTime;
	}

	@JsonIgnore
	public boolean isGod() {
		return color == LorryColor.ORANGE.getValue();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public long getLastOnAttackTime() {
		return lastOnAttackTime;
	}

	public void setLastOnAttackTime(long lastOnAttackTime) {
		this.lastOnAttackTime = lastOnAttackTime;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	@Override
	protected boolean canSeeCountryNpc(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeRobot(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeSummon(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBigBrother(VisibleObject visibleObject) {
		return true;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}
}
