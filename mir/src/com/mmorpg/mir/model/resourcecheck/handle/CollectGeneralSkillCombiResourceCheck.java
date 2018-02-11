package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.collect.resource.CollectGeneralResource;
import com.mmorpg.mir.model.collect.resource.CollectGeneralSkillCombiResource;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CollectGeneralSkillCombiResourceCheck extends ResourceCheckHandle {
	
	@Static
	private Storage<String, CollectGeneralResource> collectGeneralResources;
	
	@Static
	private Storage<String, ObjectResource> objectResources;
	
	@Static
	private Storage<String, CollectGeneralSkillCombiResource> collectGeneralSkillCombiResources;

	@Override
	public Class<?> getResourceClass() {
		return CollectGeneralSkillCombiResourceCheck.class;
	}

	@Override
	public void check() {
		for (CollectGeneralResource resource : collectGeneralResources.getAll()) {
			objectResources.get(resource.getObjectKeyOwner(), true);
		}
		
		for (CollectGeneralSkillCombiResource resource : collectGeneralSkillCombiResources.getAll()) {
			String[] ids = resource.getGeneralResourceIds();
			for (String id : ids) {
				collectGeneralResources.get(id, true);
			}
		}
		
	}

}
