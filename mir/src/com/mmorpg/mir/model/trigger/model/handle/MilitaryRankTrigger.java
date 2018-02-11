package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.service.MilitaryServiceImpl;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

public class MilitaryRankTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.MILITARY_RANK;
	}

	@Override
	public void handle(Map<String, Object> contexts, TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		MilitaryServiceImpl.getInstance().upgradeMilitaryRank(player);
	}

}
