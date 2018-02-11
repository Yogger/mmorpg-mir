package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class TriggerResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, TriggerResource> triggerResources;

	@Static
	private Storage<String, SpawnGroupResource> spawnGroupResources;

	@Override
	public Class<?> getResourceClass() {
		return TriggerResource.class;
	}

	@Override
	public void check() {
		for (TriggerResource tr : triggerResources.getAll()) {
			if (tr.getType() == TriggerType.MONSTER) {
				if (tr.getKeys().get(TriggerContextKey.SPAWNGROUPID) == null) {
					throw new RuntimeException(String.format("TriggerResource id[%s] SPAWNGROUPID不存在！ ", tr.getId()));
				}

				String spawnId = tr.getKeys().get(TriggerContextKey.SPAWNGROUPID);
				SpawnGroupResource spawnGroupResource = spawnGroupResources.get(spawnId, false);
				if (spawnGroupResource == null) {
					throw new RuntimeException(String.format("TriggerResource id[%s] SPAWNGROUPID[%s]不存在！ ",
							tr.getId(), spawnId));
				}
			}
			if (tr.getType() == null) {
				throw new RuntimeException(String.format("TriggerResource id[%s] getType[%s]不存在！ ", tr.getId(),
						tr.getType()));
			}
		}
	}

}
