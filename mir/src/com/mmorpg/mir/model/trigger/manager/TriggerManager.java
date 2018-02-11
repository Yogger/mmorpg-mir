package com.mmorpg.mir.model.trigger.manager;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class TriggerManager implements ITriggerManager{
	private static Logger logger = Logger.getLogger(TriggerManager.class);
	@Static
	private Storage<String, TriggerResource> tiggerResource;

	private static TriggerManager instance;

	private Map<TriggerType, AbstractTrigger> handles = New.hashMap();

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static TriggerManager getInstance() {
		return instance;
	}

	public void registerAbstractTrigger(AbstractTrigger handle) {
		if (handles.containsKey(handle.getType())) {
			logger.error("AbstractTrigger type[" + handle.getType() + "]重复。");
			throw new IllegalArgumentException("AbstractTrigger type[" + handle.getType() + "]重复。");
		}
		handles.put(handle.getType(), handle);
	}

	public void trigger(Map<String, Object> contexts, TriggerResource resource) {
		if (resource.getCoreConditions().verify(contexts)) {
			handles.get(resource.getType()).handle(contexts, resource);
		}
	}

	public void trigger(Map<String, Object> contexts, String id) {
		trigger(contexts, tiggerResource.get(id, true));
	}

	public void trigger(Player player, String id) {
		Map<String, Object> contexts = New.hashMap();
		contexts.put(TriggerContextKey.PLAYER, player);
		trigger(contexts, id);
	}

	public void tiggerQuest(Player player, Quest quest, String id) {
		Map<String, Object> contexts = New.hashMap();
		contexts.put(TriggerContextKey.PLAYER, player);
		contexts.put(TriggerContextKey.QUEST, quest);
		trigger(contexts, id);
	}

	public Storage<String, TriggerResource> getTiggerResource() {
		return tiggerResource;
	}

	public void setTiggerResource(Storage<String, TriggerResource> tiggerResource) {
		this.tiggerResource = tiggerResource;
	}

}
