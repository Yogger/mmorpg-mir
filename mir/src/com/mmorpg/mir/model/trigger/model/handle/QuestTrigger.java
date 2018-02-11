package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestUpdate;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class QuestTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.QUEST;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);

		String[] questIds = resource.getKeys().get(TriggerContextKey.QUESTIDS).split(",");
		QuestUpdate update = QuestUpdate.valueOf();
		List<String> questList = Arrays.asList(questIds);
		Collections.shuffle(questList);
		List<Quest> newQuests = player.getQuestPool().acceptQuests(update, player, questList,
				System.currentTimeMillis(), false, true);
		if (!newQuests.isEmpty()) {
			player.getQuestPool().refreshQuest(newQuests);
		}

	}

}
