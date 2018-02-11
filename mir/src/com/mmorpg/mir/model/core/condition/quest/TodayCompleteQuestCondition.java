package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 当天完成任务的次数大于等于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayCompleteQuestCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (!player.getQuestPool().getTodayCompletionHistory().containsKey(code)) {
			return false;
		}
		if (player.getQuestPool().getTodayCompletionHistory().get(code) >= value) {
			return true;
		}
		return false;
	}
}
