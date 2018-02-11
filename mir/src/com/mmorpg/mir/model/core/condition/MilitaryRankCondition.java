package com.mmorpg.mir.model.core.condition;

import java.util.Map;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.SkillCondition;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * 军衔等级需要达到的条件
 */
public class MilitaryRankCondition extends AbstractCoreCondition implements QuestCondition, SkillCondition,AchievementCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
		}

		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.OTHER_PLAYER);
		}

		if (player == null) {
			errorObject(object);
		}

		boolean ret = true;
		if (code.trim().equals("<=")) {
			ret = player.getMilitary().getRank() <= value;
		} else if (code.trim().equals(">=")) {
			ret = player.getMilitary().getRank() >= value;
		} else if (code.trim().equals("=")) {
			ret = player.getMilitary().getRank() == value;
		}
		if (!ret) {
			throw new ManagedException(ManagedErrorCode.MILITARY_NOT_ENOUGH);
		}

		return ret;
	}

	@Override
	protected void calculate(int num) {
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { MilitaryRankUpEvent.class };
	}

	@Override
	public Class<?>[] getSkillEvent() {
		return new Class<?>[] { MilitaryRankUpEvent.class };
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { MilitaryRankUpEvent.class };
	}

}
