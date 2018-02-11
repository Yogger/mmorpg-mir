package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.NpcController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;

public class Monster extends Npc {

	protected boolean expReduction;

	public Monster(long objId, NpcController controller, WorldPosition position) {
		super(objId, controller, position);
		controller.setOwner(this);
	}

	@Override
	public boolean isPlayerEnemy(Player player) {
		return true;
	}

	
	
	@Override
	protected boolean canSeeServant(VisibleObject visibleObject) {
		return true;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.MONSTER;
	}

	@Override
	protected boolean canSeeSummon(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBigBrother(VisibleObject visibleObject) {
		return true;
	}

	public boolean isExpReduction() {
		return expReduction;
	}

	public void setExpReduction(boolean expReduction) {
		this.expReduction = expReduction;
	}

}
