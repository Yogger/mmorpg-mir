package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class DeleteSummonTrigger extends AbstractTrigger {
	@Override
	public TriggerType getType() {
		return TriggerType.DELETE_SUMMON;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		BigBrother bigBrother = player.getBigBrother();
		if (bigBrother != null) {
			bigBrother.getController().delete();
		}

	}

}
