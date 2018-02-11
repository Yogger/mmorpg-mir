package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.knownlist.KnownList;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.MasterFollowPolicy;
import com.mmorpg.mir.model.object.route.QuestRouteStep;
import com.mmorpg.mir.model.world.WorldPosition;

public class Summon extends Npc {

	protected Player master;

	private QuestRouteStep[] questRouteSteps;

	public Summon(long objId, SummonController controller, WorldPosition position, Player master) {
		super(objId, controller, position);
		controller.setOwner(this);
		setKnownlist(new KnownList(this));
		setFollowPolicy(new MasterFollowPolicy(this));
		this.master = master;
	}

	@Override
	public String getName() {
		return master.getName() + "|" + super.getName();
	}

	@Override
	public boolean isEnemy(Creature creature) {
		return master.isEnemy(creature);
	}

	public Player getMaster() {
		return master;
	}

	public void setMaster(Player master) {
		this.master = master;
	}

	@Override
	public String toString() {
		return String.format("SUMMON ID:[%d] NAME:[%s] X:[%d] Y:[%d] MASTERID:[%d] MASTERNAME:[%s]", objectId,
				getName(), getX(), getY(), master.getObjectId(), master.getName());
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SUMMON;
	}

	@Override
	protected boolean canSeeMonster(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBoss(VisibleObject visibleObject) {
		return true;
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
	protected boolean canSeeLorry(VisibleObject visibleObject) {
		return true;
	}

	@Override
	public int getCountryValue() {
		return master.getCountryValue();
	}

	public QuestRouteStep[] getQuestRouteSteps() {
		return questRouteSteps;
	}

	public void setQuestRouteSteps(QuestRouteStep[] questRouteSteps) {
		this.questRouteSteps = questRouteSteps;
	}

}
