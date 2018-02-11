package com.mmorpg.mir.model.respawn;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;

/**
 * 重生管理器，这个管理器主要是用来做怪物重生逻辑的
 * 
 * @author zhou.liu
 * 
 */
@Component
public class ReliveServiceImpl implements ReliveService {

	@Autowired
	private ObjectManager objectManager;

	private static ReliveService self;

	@Autowired
	private TriggerManager triggerManager;

	public static ReliveService getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		self = this;
	}

	public void scheduleDecayAndReliveTask(final VisibleObject visibleObject) {
		int decayInterval = visibleObject.getSpawn().getDecayInterval() * 1000;

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (visibleObject.getSpawn().isRespawn()) {
					final World world = World.getInstance();
					if (visibleObject.getSpawn().getSpawnInterval() == -1) {
						World.getInstance().despawn(visibleObject);
						// 不重生
						return;
					}

					// 重生触发事件
					if (!ArrayUtils.isEmpty(visibleObject.getSpawn().getSpawnTriggers())) {
						Map<String, Object> contexts = new HashMap<String, Object>();
						if (visibleObject.getPosition() == null || visibleObject.getPosition().getMapRegion() == null) {
							return;
						}
						contexts.put(TriggerContextKey.MAP_INSTANCE, visibleObject.getPosition().getMapRegion()
								.getParent());
						contexts.put(TriggerContextKey.NPC, visibleObject);
						for (String id : visibleObject.getSpawn().getSpawnTriggers()) {
							triggerManager.trigger(contexts, id);
						}
					}

					if (!visibleObject.getSpawn().getConditions().verify(visibleObject)) {
						// 没有满足重新条件
						World.getInstance().despawn(visibleObject);
						return;
					}
					World.getInstance().despawn(visibleObject);

					final int interval = visibleObject.getSpawn().getSpawnInterval() * 1000;

					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							int mapId = visibleObject.getSpawn().getMapId();
							int instanceId = visibleObject.getInstanceId();
							if (!World.getInstance().getWorldMaps().get(mapId).getInstances().containsKey(instanceId)) {
								return;
							}
							int[] fxy = visibleObject.getSpawn().createXY();
							int x = fxy[0];
							int y = fxy[1];
							byte heading = visibleObject.getSpawn().createHeading();
							visibleObject.setBornX(fxy[0]);
							visibleObject.setBornY(fxy[1]);
							// 这里由对象管理器进行重生的管理
							objectManager.reliveObject(visibleObject);

							world.setPosition(visibleObject, mapId, instanceId, x, y, heading);
							visibleObject.getController().onRelive();
							world.spawn(visibleObject);

						}

					}, interval);

				} else {
					World.getInstance().despawn(visibleObject);
				}

			}
		}, decayInterval);

	}

	public void scheduleDecayTask(final VisibleObject npc) {
		int decayInterval = npc.getSpawn().getDecayInterval() * 1000;

		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				World.getInstance().despawn(npc);
			}
		}, decayInterval);
	}
}
