package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.item.resource.EquipmentCreateSoulResource;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class EquipmentCreateSoulResourceCheck extends ResourceCheckHandle {
	
	@Static
	private Storage<String, EquipmentCreateSoulResource> equipCreateSoulResources;
	
	@Static
	private Storage<String, ItemResource> itemResources;
	
	@Static
	private Storage<String, ObjectResource> objectResources;

	@Override
	public Class<?> getResourceClass() {
		return EquipmentCreateSoulResourceCheck.class;
	}

	@Override
	public void check() {
		for (EquipmentCreateSoulResource resource : equipCreateSoulResources.getAll()) {
			itemResources.get(resource.getItemKey(), true);
			objectResources.get(resource.getObjectKey(), true);
		}
	}

}
