package com.mmorpg.mir.model.resourcecheck;

import javax.annotation.PostConstruct;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

public abstract class ResourceCheckHandle {

	public abstract Class<?> getResourceClass();
	
	@Static
	private Storage<String, CoreConditionResource> conditionResources;

	@PostConstruct
	public void init() {
		ResourceCheck.handles.put(getResourceClass(), this);
	}

	public abstract void check();
	
	public CoreConditionResource[] getCoreConditionResources(String[] conditionIds) {
		CoreConditionResource[] resources = new CoreConditionResource[conditionIds.length];
		for (int i = 0; i < resources.length; i++) {
			resources[i] = conditionResources.get(conditionIds[i], true);
		}
		return resources;
	}
	
}
