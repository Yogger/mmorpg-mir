package com.mmorpg.mir.model.trigger.manager;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.Storage;

public interface ITriggerManager {

	public void registerAbstractTrigger(AbstractTrigger handle);

	public void trigger(Map<String, Object> contexts, TriggerResource resource);

	public void trigger(Map<String, Object> contexts, String id);

	public void trigger(Player player, String id);

	public void tiggerQuest(Player player, Quest quest, String id);

	public Storage<String, TriggerResource> getTiggerResource();
}
