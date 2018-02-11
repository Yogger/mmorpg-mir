package com.mmorpg.mir.model.country.controllers;

import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class AllianceSpecialNpcController extends StatusNpcController {

	private CountryId countryId;

	public AllianceSpecialNpcController(CountryId id) {
		countryId = id;
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		if (object.getObjectType() == ObjectType.PLAYER) {
			Player player = (Player) object;
			CountryFlag flag = player.getCountry().getCountryFlag();
			player.setFlagStatus(flag.getAlliance());

			if (player.getCountry().getCountryFlag().getFlagQuestType() == CountryFlagQuestType.ATTACK_WITH_ALLIANCE) {
				EffectId id = getAllianceEffectId(player);
				if (id != null) {
					player.getEffectController().setAbnormal(getAllianceEffectId(player), true);
				}
			}
		}
	}

	private EffectId getAllianceEffectId(Player player) {
		int alliance = player.getCountry().getCountryFlag().getAlliance();
		if (alliance == 1) {
			return EffectId.ALLIANCE_QI;
		} else if (alliance == 2) {
			return EffectId.ALLIANCE_CHU;
		} else if (alliance == 3) {
			return EffectId.ALLIANCE_ZHAO;
		}
		return null;
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (object.getObjectType() == ObjectType.PLAYER) {
			Player player = (Player) object;
			CountryFlag flag = CountryManager.getInstance().getCountryById(countryId).getCountryFlag();
			boolean flagSee = flag.getCountryNpc() != null && flag.getCountryNpc().getKnownList().knowns(player);
			boolean specialNpcSee = false;
			for (Creature creature : flag.getSpecifiedNpcs()) {
				if (creature.getKnownList().knowns(player)) {
					specialNpcSee = true;
					break;
				}
			}
			if (specialNpcSee || flagSee) {
				return;
			}

			player.clearFlagAllianceState();
		}
	}

}
