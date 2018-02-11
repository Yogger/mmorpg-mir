package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.QuestType;

public class NoCompleteTypeQuestCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		QuestType type = QuestType.valueOf(code);
		int maxLimit = value;
		if (player.getQuestPool().getTypeCompleteCount(type) < maxLimit) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}
}
