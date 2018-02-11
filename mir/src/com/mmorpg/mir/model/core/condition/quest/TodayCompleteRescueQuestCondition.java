package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.RescueEvent;

/**
 * 当天完成营救任务的次数大于等于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayCompleteRescueQuestCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if(player == null){
			this.errorObject(object);
		}
		if (player.getRescue().getTodayCompleteCount() >= value) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { RescueEvent.class };
	}
}
