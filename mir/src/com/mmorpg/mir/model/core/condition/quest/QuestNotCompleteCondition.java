package com.mmorpg.mir.model.core.condition.quest;

import java.util.Map;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.windforce.common.utility.JsonUtils;

public class QuestNotCompleteCondition extends AbstractCoreCondition {

	private String[] questIds;

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
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);

		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getQuestPool().getCompletionHistory().containsKey(questIds[player.getCountryValue() - 1])) {
			return false;
		}

		return true;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		questIds = JsonUtils.string2Array(resource.getCode(), String.class);
		if (questIds.length > CountryId.values().length) {
			throw new RuntimeException(String.format("ConditionType QuestNotCompleteCondition init fail, code[%s]",
					resource.getCode()));
		}
	}

}
