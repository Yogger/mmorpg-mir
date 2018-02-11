package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;

/**
 * 当天完成任务的次数少于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayCompleteNotQuestCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getQuestPool().getTodayCompletionHistory().containsKey(code)) {
			return true;
		}
		if (player.getQuestPool().getTodayCompletionHistory().get(code) < value) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { AnotherDayEvent.class };
	}
}
