package com.mmorpg.mir.model.resourcecheck.handle;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.rescue.resource.RescueResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class RescueResourceCheck extends ResourceCheckHandle {

	@Static
	private Storage<String, SpawnGroupResource> spawnGroups;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;

	@Static
	private Storage<String, RescueResource> rescueResources;

	@Static
	private Storage<String, ObjectResource> objectResources;

	@Override
	public Class<?> getResourceClass() {
		return RescueResource.class;
	}

	private static final int MAX_ROUND = 5;

	@Override
	public void check() {
		for (RescueResource rescueResource : rescueResources.getAll()) {
			if (rescueResource.getIndex() > MAX_ROUND) {
				// 环数大于5
				throw new RuntimeException(String.format("营救任务环数[%s]不得大于5", String.valueOf(rescueResource.getIndex())));
			}

			if (StringUtils.isBlank(rescueResource.getChooserRewardId())) {
				throw new RuntimeException(String.format("id[%s]营救任务奖励id为空", rescueResource.getId()));
			}
			ChooserGroup rewardChooser = chooserGroups.get(rescueResource.getChooserRewardId(), false);
			if (null == rewardChooser) {
				throw new RuntimeException(String.format("id[%s]对应任务奖励选择id[%s]不存在", rescueResource.getId(),
						rescueResource.getChooserRewardId()));
			}

			if (!StringUtils.isBlank(rescueResource.getGatherId())) {
				ObjectResource gatherObjectResource = objectResources.get(rescueResource.getGatherId(), false);
				if (null == gatherObjectResource) {
					throw new RuntimeException(String.format("id[%s]对应采集物id[%s]不存在", rescueResource.getId(),
							rescueResource.getGatherId()));
				}
			}

			if (!StringUtils.isBlank(rescueResource.getMonsterId())) {
				SpawnGroupResource monsterSpawn = spawnGroups.get(rescueResource.getMonsterId(), false);
				if (null == monsterSpawn) {
					throw new RuntimeException(String.format("id[%s]对应怪物id[%s]不存在", rescueResource.getId(),
							rescueResource.getMonsterId()));
				}
			}

			String sid = rescueResource.getId();
			int id = Integer.parseInt(sid);

			int realIndex = id % MAX_ROUND == 0 ? MAX_ROUND : id % MAX_ROUND;

			if (rescueResource.getIndex() != realIndex) {
				throw new RuntimeException(String.format("id[%s]当前所处环数错误", rescueResource.getId()));
			}
			if (id % MAX_ROUND == 0) {
				if (!StringUtils.isBlank(rescueResource.getNextId())) {
					throw new RuntimeException(String.format("id[%s]下一任务id应为空", rescueResource.getId()));
				}
			} else {
				if (StringUtils.isBlank(rescueResource.getNextId())) {
					throw new RuntimeException(String.format("id[%s]下一任务id不应为空", rescueResource.getId()));
				}
				int nextId = Integer.parseInt(rescueResource.getNextId());
				if (id + 1 != nextId) {
					throw new RuntimeException(String.format("id[%s]下一任务id配置错误", rescueResource.getId()));
				}
			}
		}
	}
}
