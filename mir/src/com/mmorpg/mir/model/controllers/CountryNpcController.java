package com.mmorpg.mir.model.controllers;

import com.mmorpg.mir.model.SpringComponentStation;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class CountryNpcController extends NpcController {

	// private boolean respwan = true;

	@Override
	public CountryNpc getOwner() {
		return (CountryNpc) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Monster owner = getOwner();

		// 这里可能要用来处理掉落和经验
		doReward(lastAttacker, owner.getAggroList());

		owner.getAi().handleEvent(Event.DIED);
		owner.getController().onFightOff();
		// deselect target at the end
		owner.setTarget(null);

		// 调用重生计划任务
		scheduleRelive();

	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (!getOwner().isGod()) {
			super.onAttack(creature, skillId, damage, damageResult);
			int noticeGroup = getOwner().getSpawn().getNoticeGroup();
			if (noticeGroup == 1) {
				Country country = CountryManager.getInstance().getCountries()
						.get(CountryId.valueOf(getOwner().getCountryValue()));
				Long time = country.getGuardNoticeCDMap().get(noticeGroup);
				if (time == null || time + 30000L < System.currentTimeMillis()) {
					if (creature instanceof Player) {
						country.getGuardNoticeCDMap().put(noticeGroup, System.currentTimeMillis());
					} else if (creature instanceof Summon) {
						country.getGuardNoticeCDMap().put(noticeGroup, System.currentTimeMillis());
					}
				}
			} else if (noticeGroup == 2) {
				Country country = CountryManager.getInstance().getCountries()
						.get(CountryId.valueOf(getOwner().getCountryValue()));
				Long time = country.getGuardNoticeCDMap().get(noticeGroup);
				if (time == null || time + 30000L < System.currentTimeMillis()) {
					if (creature instanceof Player) {
						country.getGuardNoticeCDMap().put(noticeGroup, System.currentTimeMillis());
					} else if (creature instanceof Summon) {
						country.getGuardNoticeCDMap().put(noticeGroup, System.currentTimeMillis());
					}
				}
			}
		}
	}

	private void scheduleRelive() {
		if (getOwner().getSpawn().isRespawn()) {
			SpringComponentStation.getReliveService().scheduleDecayAndReliveTask(getOwner());
		} else {
			SpringComponentStation.getReliveService().scheduleDecayTask(getOwner());
		}
	}

}
