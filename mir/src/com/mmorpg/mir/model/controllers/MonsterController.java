package com.mmorpg.mir.model.controllers;

import com.mmorpg.mir.model.SpringComponentStation;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;

public class MonsterController extends NpcController {

	private boolean respwan = true;

	@Override
	public Monster getOwner() {
		return (Monster) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Monster owner = getOwner();

		if (lastAttacker instanceof Player) {
			((Player) lastAttacker).getLifeStats().increaseDp(
					ConfigValueManager.getInstance().MONSTER_DIE_DP.getValue());
		} else if (lastAttacker instanceof Summon) {
			((Summon) lastAttacker).getMaster().getLifeStats()
					.increaseDp(ConfigValueManager.getInstance().MONSTER_DIE_DP.getValue());
		}

		// 这里可能要用来处理掉落和经验
		doReward(lastAttacker, owner.getAggroList());

		owner.getAi().handleEvent(Event.DIED);
		owner.getController().onFightOff();
		// deselect target at the end
		owner.setTarget(null);

		// 调用重生计划任务
		scheduleRelive();

	}

	private void scheduleRelive() {
		if (respwan) {
			SpringComponentStation.getReliveService().scheduleDecayAndReliveTask(getOwner());
		} else {
			SpringComponentStation.getReliveService().scheduleDecayTask(getOwner());
		}
	}

	public boolean isRespwan() {
		return respwan;
	}

	public void setRespwan(boolean respwan) {
		this.respwan = respwan;
	}

}
