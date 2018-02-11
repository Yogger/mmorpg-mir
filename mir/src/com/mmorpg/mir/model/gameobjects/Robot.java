package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.ai.AIUtil;
import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class Robot extends CountryNpc {

	public Robot(long objId, CountryNpcController controller, WorldPosition position, int country) {
		super(objId, controller, position, country);
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.COUNTRY_NPC;
	}

	@Override
	public boolean findEnemy() {
		return AIUtil.addClostHead(this);
	}

	@Override
	protected boolean isRobotEnemy(Robot creature) {
		return getCountry() != creature.getCountry();
	}

	@Override
	protected boolean canSeeRobot(VisibleObject visibleObject) {
		return true;
	}
}
