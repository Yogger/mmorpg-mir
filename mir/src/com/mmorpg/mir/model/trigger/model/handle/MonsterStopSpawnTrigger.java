package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class MonsterStopSpawnTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.MONSTER_STOP_SPAWN;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final WorldMapInstance worldMapInstance = (WorldMapInstance) contexts.get(TriggerContextKey.MAP_INSTANCE);

		// 有延迟执行的
		if (resource.getKeys().containsKey(TriggerContextKey.DELAY)) {
			Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					worldMapInstance.monsterStopSpawn();
				}
			}, Integer.valueOf(resource.getKeys().get(TriggerContextKey.DELAY)));
			worldMapInstance.getTriggerTasks().add(future);
			return;
		}
		worldMapInstance.monsterStopSpawn();
	}
}
