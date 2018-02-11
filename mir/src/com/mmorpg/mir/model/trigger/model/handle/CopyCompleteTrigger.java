package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class CopyCompleteTrigger extends AbstractTrigger {
	@Override
	public TriggerType getType() {
		return TriggerType.COPY_COMPLETE;
	}

	@Override
	public void handle(Map<String, Object> contexts,
			final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		String copyId = resource.getKeys().get(TriggerContextKey.COPYID);
		int completeTime = 0;
		if (contexts.containsKey(TriggerContextKey.QUEST)) {
			Quest quest = (Quest) contexts.get(TriggerContextKey.QUEST);
			completeTime = (int) (System.currentTimeMillis() - quest
					.getCreateTime());
		}

		player.getCopyHistory().addTodayCompleteCount(player,copyId, 1, true,
				completeTime,true);
	}

}
