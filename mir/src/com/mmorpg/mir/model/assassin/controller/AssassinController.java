package com.mmorpg.mir.model.assassin.controller;

import com.mmorpg.mir.model.assassin.manager.AssassinManager;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.controllers.attack.AggroList;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class AssassinController extends BossController {
	@Override
	public void onAttack(Creature lastAttacker, int skillId, long damage, DamageResult damageResult) {
		super.onAttack(lastAttacker, skillId, damage, damageResult);
	}

	@Override
	protected void doReward(Creature lastAttacker, AggroList aggroList) {
		// TODO Auto-generated method stub
		super.doReward(lastAttacker, aggroList);
		AssassinManager.getInstance().die(lastAttacker);
	}
	
}
