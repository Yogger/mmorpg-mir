package com.mmorpg.mir.model.ministerfete.controller;

import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.controllers.attack.AggroList;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.ministerfete.manager.MinisterFeteManager;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class MinisterFeteController extends BossController {
	@Override
	public void onAttack(Creature lastAttacker, int skillId, long damage, DamageResult damageResult) {
		super.onAttack(lastAttacker, skillId, damage, damageResult);
	}

	@Override
	protected void doReward(Creature lastAttacker, AggroList aggroList) {
		super.doReward(lastAttacker, aggroList);
		MinisterFeteManager.getInstance().die(lastAttacker);
	}
	
}
