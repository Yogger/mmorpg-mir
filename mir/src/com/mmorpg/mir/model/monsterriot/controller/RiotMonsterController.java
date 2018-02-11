package com.mmorpg.mir.model.monsterriot.controller;

import com.mmorpg.mir.model.controllers.MonsterController;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.monsterriot.manager.MonsterRiotManager;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class RiotMonsterController extends MonsterController {

	private CountryId countryId;

	public RiotMonsterController(CountryId id) {
		this.countryId = id;
	}

	@Override
	public void onAttack(Creature lastAttacker, int skillId, long damage, DamageResult damageResult) {
		super.onAttack(lastAttacker, skillId, damage, damageResult);

		Player attackerPlayer = null;
		// 这个应该不可能发生
		if (lastAttacker instanceof Player) {
			attackerPlayer = (Player) lastAttacker;
		} else if (lastAttacker instanceof Summon) {
			attackerPlayer = ((Summon) lastAttacker).getMaster();
		}

		if (attackerPlayer != null && getOwner().getSpawn().getCountry() != attackerPlayer.getCountryValue()) {
			return;
		}

		MonsterRiotManager.getInstance().monsterKill(attackerPlayer, countryId, damage);
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);

		MonsterRiotManager.getInstance().triggerEnd(countryId);

	}
}
