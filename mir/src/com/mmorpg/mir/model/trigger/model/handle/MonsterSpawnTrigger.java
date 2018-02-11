package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class MonsterSpawnTrigger extends AbstractTrigger {

	@Autowired
	private SpawnManager spawnManager;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@Override
	public TriggerType getType() {
		return TriggerType.MONSTER;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final SpawnGroupResource sgr = spawnManager.getSpawn(resource.getKeys().get(TriggerContextKey.SPAWNGROUPID));
		WorldMapInstance worldMapInstance = (WorldMapInstance) contexts.get(TriggerContextKey.MAP_INSTANCE);
		final int instanceId = worldMapInstance.getInstanceId();

		final int num = resource.getKeys().containsKey(TriggerContextKey.NUM) ? Integer.valueOf(resource.getKeys().get(
				TriggerContextKey.NUM)) : 0;
		// 有延迟执行的
		if (resource.getKeys().containsKey(TriggerContextKey.DELAY)
				&& Integer.valueOf(resource.getKeys().get(TriggerContextKey.DELAY)).intValue() != 0) {
			Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					spawnManager.spawn(sgr, instanceId, num);
				}
			}, Integer.valueOf(resource.getKeys().get(TriggerContextKey.DELAY)));
			worldMapInstance.getTriggerTasks().add(future);
			return;
		}

		// CRON表达式执行
		if (resource.getKeys().containsKey(TriggerContextKey.CRON)) {
			Future<?> future = simpleScheduler.schedule(new ScheduledTask() {
				@Override
				public void run() {
					spawnManager.spawn(sgr, instanceId, num);
				}

				@Override
				public String getName() {
					return String.format("触发器[%s]启动刷怪任务[%s]", resource.getId(), sgr.getKey());
				}

			}, resource.getKeys().get(TriggerContextKey.CRON));
			worldMapInstance.getTriggerTasks().add(future);
		}

		// 立即执行
		spawnManager.spawn(sgr, instanceId, num);
	}
}
