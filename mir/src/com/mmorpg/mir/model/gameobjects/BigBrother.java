package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class BigBrother extends Summon {

	private long createTime;

	public BigBrother(long objId, SummonController controller, WorldPosition position, Player master) {
		super(objId, controller, position, master);
		this.master.setBigBrother(this);
		createTime = System.currentTimeMillis();
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.BIGBROTHER;
	}

	@Override
	protected boolean canSeePlayer(VisibleObject visibleObject) {
		Player player = (Player) visibleObject;
		return master == player;
	}

	public boolean isNew() {
		return System.currentTimeMillis() - createTime < 3000;
	}
}
