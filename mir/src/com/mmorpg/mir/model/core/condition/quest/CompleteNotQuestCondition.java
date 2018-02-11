package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 完成任务的次数少于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CompleteNotQuestCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getQuestPool().getCompletionHistory().containsKey(code)) {
			return true;
		}
		if (player.getQuestPool().getCompletionHistory().get(code) < value) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
