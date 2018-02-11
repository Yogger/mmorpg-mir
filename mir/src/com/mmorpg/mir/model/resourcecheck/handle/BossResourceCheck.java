package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class BossResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, SpawnGroupResource> spawnGroupResources;

	@Static
	private Storage<String, BossResource> bossResource;

	@Override
	public Class<?> getResourceClass() {
		return BossResource.class;
	}

	@Override
	public void check() {
		for (BossResource rs : bossResource.getAll()) {
			if (!rs.isElite()) {
				SpawnGroupResource tombstone = spawnGroupResources.get(rs.getTombstoneSpawnId(), false);
				if (tombstone == null) {
					throw new RuntimeException(String.format("BossResource id[%s]  getTombstoneSpawnId[%s]找不到！",
							rs.getId(), rs.getTombstoneSpawnId()));
				}
			}
		}
	}
}
