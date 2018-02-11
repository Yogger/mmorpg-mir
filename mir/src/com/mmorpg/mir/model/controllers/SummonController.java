package com.mmorpg.mir.model.controllers;

import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Summon;

public class SummonController extends NpcController {

	@Override
	public Summon getOwner() {
		return (Summon) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Summon owner = getOwner();

		owner.getAi().handleEvent(Event.DIED);
		owner.getController().onFightOff();

		owner.setTarget(null);

		delete();
	}

}
