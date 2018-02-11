package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class Servant extends Summon {

	public Servant(long objId, SummonController controller, WorldPosition position, Player master) {
		super(objId, controller, position, master);
	}

	@Override
	public boolean isEnemy(Creature creature) {
		if (creature instanceof Monster) {
			return master.isEnemy(creature);
		}
		return false;
	}

	@Override
	protected boolean isTownPlayerNpcEnemy(TownPlayerNpc townPlayerNpc) {
		return false;
	}

	@Override
	protected boolean canSeeLorry(VisibleObject visibleObject) {
		return false;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SERVANT;
	}
}
