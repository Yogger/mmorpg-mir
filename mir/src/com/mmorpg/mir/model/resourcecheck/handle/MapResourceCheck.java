package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class MapResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<Integer, MapResource> mapResources;

	@Static
	private Storage<Integer, ReliveBaseResource> reliveBaseResources;

	@Override
	public Class<?> getResourceClass() {
		return MapResource.class;
	}

	@Override
	public void check() {
		for (MapResource mr : mapResources.getAll()) {
			ReliveBaseResource baseResource = reliveBaseResources.get(mr.getReliveBaseResourceId(), false);
			if (baseResource == null) {
				throw new RuntimeException(String.format("MapResource id[%s] reliveBaseResourceId[%s]不存在！ ",
						mr.getMapId(), mr.getReliveBaseResourceId()));
			}
			if (mr.getForbidEnter() == null) {
				throw new RuntimeException(String.format("MapResource id[%s] getForbidEnter[%s]不存在！ ", mr.getMapId(),
						mr.getForbidEnter()));
			}
			if (mr.getFileName() == null) {
				throw new RuntimeException(String.format("MapResource id[%s] getFileName[%s]不存在！ ", mr.getMapId(),
						mr.getForbidEnter()));
			}
		}
	}

}
