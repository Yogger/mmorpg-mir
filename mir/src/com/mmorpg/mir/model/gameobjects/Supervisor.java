package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.SupervisorController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class Supervisor extends VisibleObject {

	public Supervisor(long objId, SupervisorController controller, WorldPosition position) {
		super(objId, controller, position);
		controller.setOwner(this);
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SUPERVISOR;
	}

	@Override
	protected boolean canSeePlayer(VisibleObject visibleObject) {
		return true;
	}

	@Override
	public String getName() {
		return "";
	}

}
