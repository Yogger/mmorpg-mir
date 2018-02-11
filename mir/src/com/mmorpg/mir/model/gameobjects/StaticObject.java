package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.StaticObjectController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class StaticObject extends VisibleObject {

	public StaticObject(long objectId, StaticObjectController controller, WorldPosition position) {
		super(objectId, controller, position);
		controller.setOwner(this);
	}

	public static StaticObject valueOf(int objId, StaticObjectController controller, WorldPosition position) {
		StaticObject result = new StaticObject(objId, controller, position);
		// TODO 参见player的valueOf方法
		return result;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.STATICOBJECT;
	}

}
