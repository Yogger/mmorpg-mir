package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class ReliveBaseResourceCheck extends ResourceCheckHandle {

	@Static
	private Storage<Integer, ReliveBaseResource> reliveBaseResources;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;

	@Override
	public Class<?> getResourceClass() {
		return MapResource.class;
	}

	@Override
	public void check() {
		for (ReliveBaseResource mr : reliveBaseResources.getAll()) {
			ChooserGroup chooserGroup = chooserGroups.get(mr.getChooserGroupId(), false);
			if (chooserGroup == null) {
				throw new RuntimeException(String.format("ReliveBaseResource id[%s] chooserGroup[%s]不存在！ ",
						mr.getReliveId(), mr.getChooserGroupId()));
			}
		}
	}

}
