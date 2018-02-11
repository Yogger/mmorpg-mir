package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestPhase;
import com.windforce.common.utility.JsonUtils;

public class CurrentQuestHasCompleteCondition extends AbstractCoreCondition {

	private String[] questIds;

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
		if (player == null) {
			this.errorObject(object);
		}
		for (String questId : questIds) {
			Quest quest = player.getQuestPool().getQuests().get(questId);
			if (quest == null || quest.getPhase() != QuestPhase.COMPLETE) {
				return false;
			}
		}

		return true;
	}

	@Override
	protected void init() {
		super.init();
		questIds = JsonUtils.string2Array(code, String.class);
	}

}
