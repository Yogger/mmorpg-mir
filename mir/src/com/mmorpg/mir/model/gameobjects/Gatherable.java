package com.mmorpg.mir.model.gameobjects;

import java.util.concurrent.atomic.AtomicLong;

import com.mmorpg.mir.model.controllers.GatherableController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class Gatherable extends StaticObject {

	private String rewardId;

	private int interval;

	private AtomicLong ownerId = new AtomicLong(0);

	public Gatherable(long objId, GatherableController controller, WorldPosition position) {
		super(objId, controller, position);
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.GATHERABLE;
	}

	@Override
	public GatherableController getController() {
		return (GatherableController) super.getController();
	}

	public boolean setOwner(Player player) {
		return ownerId.compareAndSet(0, player.getObjectId());
	}

	public void reset() {
		ownerId.set(0);
	}

}