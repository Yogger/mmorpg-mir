package com.mmorpg.mir.model.gameobjects;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.h2.util.New;

import com.mmorpg.mir.model.controllers.DropObjectController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldPosition;

public class DropObject extends StaticObject {

	private RewardItem rewardItem;
	/** 物品归属 */
	private List<Long> ownership;
	/** 掉落物品消失任务 **/
	private Future<?> clearTask;

	private AtomicBoolean pick = new AtomicBoolean(false);

	private int clearTime = 10000;

	private final long createTime = System.currentTimeMillis();
	
	private String fromNpcSpawnKey;

	public DropObject(long objId, DropObjectController controller, WorldPosition position) {
		super(objId, controller, position);
	}

	public static DropObject valueOf(long objId, DropObjectController controller, WorldPosition position,
			RewardItem rewardItem, int clearTime, String sourcerSpawnKey) {
		final DropObject obj = new DropObject(objId, controller, position);
		obj.clearTime = clearTime;
		obj.rewardItem = rewardItem;
		obj.fromNpcSpawnKey = sourcerSpawnKey;
		return obj;
	}

	public void addOwnership(long playerId) {
		if (ownership == null) {
			ownership = New.arrayList();
		}
		ownership.add(playerId);
	}

	public RewardItem getRewardItem() {
		return rewardItem;
	}

	public void setRewardItem(RewardItem rewardItem) {
		this.rewardItem = rewardItem;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.DROPOBJECT;
	}

	public boolean inOwnership(long playerId) {
		if (ownership == null || ownership.isEmpty()) {
			return true;
		}
		return ownership.contains(playerId);
	}
	
	public boolean isEveryOneCanPick() {
		return ownership == null || ownership.isEmpty(); 
	}

	public List<Long> getOwnership() {
		return ownership;
	}

	public void setOwnership(List<Long> ownership) {
		this.ownership = ownership;
	}

	public boolean pickUp() {
		return pick.compareAndSet(false, true);
	}

	public void startClearTask() {
		if (clearTask == null) {
			final VisibleObject obj = this;
			clearTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					World.getInstance().despawn(obj);
				}
			}, clearTime);
		}
	}

	public void cancelAllTask() {
		if (clearTask != null && !clearTask.isCancelled()) {
			clearTask.cancel(false);
			this.clearTask = null;
		}
	}

	public boolean isNew() {
		return (System.currentTimeMillis() - createTime) < 1000;
	}

	public String getFromNpcSpawnKey() {
		return fromNpcSpawnKey;
	}

	public void setFromNpcSpawnKey(String fromNpcSpawnKey) {
		this.fromNpcSpawnKey = fromNpcSpawnKey;
	}

}
