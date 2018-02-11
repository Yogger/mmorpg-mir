package com.mmorpg.mir.model.world.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.countrycopy.worldmap.CountryCopyWorldMapInstance;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;

/**
 * 地图实例管理器
 * 
 * @author xiaosan
 * 
 */
public class MapInstanceService {
	private static final Logger log = Logger.getLogger(MapInstanceService.class);

	private static final int DEFAULT_CHECK_TIME = 10000;

	public static WorldMapInstance createCommonMapCopy(int mapId) {
		WorldMapInstance instance = getNextAvailableInstance(mapId);
		startInstanceChecker(instance);
		return instance;
	}

	public static WorldMapInstance createCommonMapCopy(int mapId, boolean spawnMonster) {
		WorldMapInstance instance = getNextAvailableInstance(mapId, spawnMonster);
		if (spawnMonster) {
			startInstanceChecker(instance);
		}
		return instance;
	}

	public static WorldMapInstance createOrLoadGangOfWarMapCopy(int mapId, int instanceId) {
		WorldMap map = World.getInstance().getWorldMap(mapId);
		if (map.getInstances().containsKey(instanceId)) {
			return map.getInstances().get(instanceId);
		}
		WorldMapInstance instance = null;
		synchronized (map) {
			log.info(String.format("Creating new map [%s] instance [%s] ", mapId, instanceId));
			WorldMapInstance worldMapInstance = map.createNewInstance(instanceId);
			instance = worldMapInstance;
		}
		return instance;
	}

	public static CountryCopyWorldMapInstance createCountryCopyMap(int mapId, int instanceId, CountryCopy countryCopy) {
		WorldMap map = World.getInstance().getWorldMap(mapId);
		CountryCopyWorldMapInstance instance = null;
		synchronized (map) {
			log.info(String.format("Creating new map [%s] instance [%s] ", mapId, instanceId));
			CountryCopyWorldMapInstance worldMapInstance = new CountryCopyWorldMapInstance(map, instanceId, countryCopy);
			map.addInstance(worldMapInstance.getInstanceId(), worldMapInstance);
			instance = worldMapInstance;
		}
		return instance;
	}

	public static WorldMapInstance getNextAvailableInstance(int mapId) {
		return getNextAvailableInstance(mapId, true);
	}

	public static WorldMapInstance getNextAvailableInstance(int mapId, boolean spawnMonster) {
		WorldMap map = World.getInstance().getWorldMap(mapId);

		synchronized (map) {

			WorldMapInstance worldMapInstance = map.createNewInstance();
			if (spawnMonster) {
				SpawnManager.getInstance().spawnInstance(mapId, worldMapInstance.getInstanceId());
			}
			log.info(String.format("Creating new map [%s] instance [%s] ", mapId, worldMapInstance.getInstanceId()));

			return worldMapInstance;
		}
	}

	public static void destroyInstance(WorldMapInstance instance) {
		if (instance.getEmptyInstanceTask() != null) {
			instance.getEmptyInstanceTask().cancel(false);
		}
		instance.stopTasks();

		WorldMap map = instance.getParent();

		int instanceId = instance.getInstanceId();

		map.removeWorldMapInstance(instanceId);

		log.info(String.format("Destroying map [%s] instance [%s] ", map.getMapId(), instanceId));

		Iterator<VisibleObject> it = instance.objectIterator();
		while (it.hasNext()) {
			VisibleObject obj = it.next();
			if (obj instanceof Player) {
				// Player player = (Player) obj;
				// TODO 这里应该是强制踢出玩家s
			} else if (!(obj instanceof Summon)) {
				obj.getController().delete();
			}
		}
		instance.setDestory(true);
	}

	public static void registerPlayerWithInstance(WorldMapInstance instance, Player player) {
		instance.register(player.getObjectId());
	}

	public static WorldMapInstance getRegisteredInstance(int mapId, long objectId) {
		Iterator<WorldMapInstance> iterator = World.getInstance().getWorldMap(mapId).iterator();
		while (iterator.hasNext()) {
			WorldMapInstance instance = iterator.next();
			if (instance.isRegistered(objectId))
				return instance;
		}
		return null;
	}

	public static WorldMapInstance getRegisteredInstanceByCopyId(int mapId, long objectId, String copyId) {
		Iterator<WorldMapInstance> iterator = World.getInstance().getWorldMap(mapId).iterator();
		while (iterator.hasNext()) {
			WorldMapInstance instance = iterator.next();
			if (instance.isRegistered(objectId)) {
				if (instance.getCopyInfo() != null && copyId.equals(instance.getCopyInfo().getCopyId())) {
					return instance;
				}
			}
		}
		return null;
	}

	public static WorldMapInstance getRegisteredNotRewardInstanceByCopyId(int mapId, long objectId, String copyId) {
		Iterator<WorldMapInstance> iterator = World.getInstance().getWorldMap(mapId).iterator();
		while (iterator.hasNext()) {
			WorldMapInstance instance = iterator.next();
			if (instance.isRegistered(objectId)) {
				if (instance.getCopyInfo() != null && copyId.equals(instance.getCopyInfo().getCopyId())
						&& !instance.getCopyInfo().isReward()) {
					return instance;
				}
			}
		}
		return null;
	}

	public static boolean isInstanceExist(int worldId, int instanceId) {
		return World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(instanceId) != null;
	}

	public static void startInstanceChecker(WorldMapInstance worldMapInstance) {
		worldMapInstance.setEmptyInstanceTask(ThreadPoolManager.getInstance().scheduleAtFixedRate(
				new EmptyInstanceCheckerTask(worldMapInstance), DEFAULT_CHECK_TIME * 6, DEFAULT_CHECK_TIME));
	}

	private static class EmptyInstanceCheckerTask implements Runnable {
		private final WorldMapInstance worldMapInstance;

		private EmptyInstanceCheckerTask(WorldMapInstance worldMapInstance) {
			this.worldMapInstance = worldMapInstance;
		}

		@Override
		public void run() {
			// 如果这个地图没有人的话，就删除
			if (worldMapInstance.getPlayerCount() == 0 && worldMapInstance.getCopyInfo().destory()) {
				destroyInstance(worldMapInstance);
				return;
			}

			// 清理掉cancelled和done的任务
			if (worldMapInstance.getTriggerTasks() != null) {
				List<Future<?>> removeFutures = new ArrayList<Future<?>>();
				for (Future<?> future : worldMapInstance.getTriggerTasks()) {
					if (future.isCancelled() || future.isDone()) {
						removeFutures.add(future);
					}
				}

				for (Future<?> future : removeFutures) {
					worldMapInstance.getTriggerTasks().remove(future);
				}
			}

			if (!worldMapInstance.getCallbackFutures().isEmpty()) {
				List<String> removeFutureKeys = new ArrayList<String>();
				for (Entry<String, Future<?>> entry : worldMapInstance.getCallbackFutures().entrySet()) {
					if (entry.getValue().isCancelled() || entry.getValue().isDone()) {
						removeFutureKeys.add(entry.getKey());
					}
				}
				for (String key : removeFutureKeys) {
					worldMapInstance.getCallbackFutures().remove(key);
				}
			}

		}
	}
}
