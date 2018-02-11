package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.GiftCollectCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.windforce.common.utility.JsonUtils;

public class TodayQuestCompleteByCountryCondition extends AbstractCoreCondition implements GiftCollectCondition {

	private String[] questIds;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		Integer count = player.getQuestPool().getTodayCompletionHistory().get(questIds[player.getCountryValue() - 1]);
		if(count==null){
			return false;
		}
		if (count >= value) {
			return true;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		questIds = JsonUtils.string2Array(code, String.class);
	}

	@Override
	public Class<?>[] getGiftCollectEvent() {
		return new Class<?>[] { QuestCompletEvent.class };
	}
}
