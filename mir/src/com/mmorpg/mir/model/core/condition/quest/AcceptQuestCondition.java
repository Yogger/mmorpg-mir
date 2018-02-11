package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 接取任务的次数少于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class AcceptQuestCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getQuestPool().getAcceptHistory().containsKey(code)) {
			return true;
		}
		if (player.getQuestPool().getAcceptHistory().get(code) < value) {
			return true;
		}
		return false;
	}
}
