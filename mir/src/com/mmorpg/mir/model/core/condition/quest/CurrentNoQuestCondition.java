package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 当前没有该任务
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CurrentNoQuestCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if(player ==  null){
			this.errorObject(object);
		}
		if (!player.getQuestPool().getQuests().containsKey(code)) {
			return true;
		}
		return false;
	}
}
