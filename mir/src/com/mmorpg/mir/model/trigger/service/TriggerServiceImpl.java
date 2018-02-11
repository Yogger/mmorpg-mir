package com.mmorpg.mir.model.trigger.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class TriggerServiceImpl implements TriggerService {

	@Autowired
	private TriggerManager triggerManager;

	public void clientTrigger(String id, Player player) {
		TriggerResource resource = triggerManager.getTiggerResource().get(id, true);
		if (!resource.isClientTrigger()) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (!resource.getCoreConditions().verify(player, true)) {
			return;
		}
		Map<String, Object> contexts = new HashMap<String, Object>();
		contexts.put("PLAYER", player);
		triggerManager.trigger(contexts, resource);
	}
}
