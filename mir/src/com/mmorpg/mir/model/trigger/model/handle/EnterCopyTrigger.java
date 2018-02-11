package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.service.CopyService;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class EnterCopyTrigger extends AbstractTrigger {

	@Autowired
	private CopyService copyService;

	@Override
	public TriggerType getType() {
		return TriggerType.ENTER_COPY;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		String copyId = (String) contexts.get(TriggerContextKey.COPYID);
		copyService.enterCopy(copyId, player, true);
	}
}
