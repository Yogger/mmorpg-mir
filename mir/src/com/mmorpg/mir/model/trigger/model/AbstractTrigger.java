package com.mmorpg.mir.model.trigger.model;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

public abstract class AbstractTrigger {
	protected static Logger logger = Logger.getLogger(AbstractTrigger.class);
	
	public abstract TriggerType getType();

	@Autowired
	private TriggerManager triggerManager;

	public abstract void handle(Map<String, Object> contexts, TriggerResource resource);

	@PostConstruct
	public void init() {
		triggerManager.registerAbstractTrigger(this);
	}

}
