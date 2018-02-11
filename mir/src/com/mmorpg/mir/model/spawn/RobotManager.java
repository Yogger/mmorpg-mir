package com.mmorpg.mir.model.spawn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class RobotManager implements IRobotManager{

	@Autowired
	private World world;
	@Autowired
	private ObjectManager objectManager;

	private Map<ObjectType, Integer> objectMap = new HashMap<ObjectType, Integer>();

	@Static
	private Storage<String, SpawnGroupResource> spawnGroupResourceStorage;

	@Static
	private Storage<String, ObjectResource> objectResourceStorage;

	private static final Logger log = Logger.getLogger(RobotManager.class);

	private static RobotManager self;

	public static RobotManager getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		self = this;
	}

	public void spawnRobotAll() {
		for (WorldMap map : world.getWorldMaps().values()) {
			for (WorldMapInstance instance : map.getInstances().values()) {
				spawnRobotInstance(map.getMapId(), instance.getInstanceId());
			}
		}

		for (Entry<ObjectType, Integer> entry : objectMap.entrySet()) {
			log.warn("Loaded " + entry.getValue() + " " + entry.getKey() + "  spawns");
		}
	}

	public void spawnRobotInstance(int mapId, int instanceIndex) {
		// 这里根据地图id获取对应的生物资源列表
		for (SpawnGroupResource resource : spawnGroupResourceStorage.getIndex("mapId", mapId)) {
			if (!resource.isNoAutoSpawn()) {
				ObjectResource oresource = objectResourceStorage.get(resource.getObjectKey(), true);
				if (oresource.getObjectType() == ObjectType.MONSTER) {
					int size = spawnRobot(resource, instanceIndex);
					log.warn("create  " + size + " robot in map " + mapId + "  instance " + instanceIndex);
				}
			}

		}
	}

	public int spawnRobot(SpawnGroupResource resource, int instanceIndex) {
		int result = 0;
		for (int i = 0, j = resource.getNum() * Debug.opps; i < j; i++) {
			VisibleObject object = spawnRobotObject(resource, instanceIndex);

			ObjectType ot = object.getObjectType();
			if (!objectMap.containsKey(ot)) {
				objectMap.put(ot, 0);
			}

			objectMap.put(ot, objectMap.get(ot) + 1);

			result++;
		}
		return result;
	}

	public VisibleObject spawnRobotObject(SpawnGroupResource spawn, int instanceIndex, Object... args) {
		VisibleObject object = objectManager.createRobotObject(spawn, instanceIndex, args);
		if (object != null) {
			object.setSpawnKey(spawn.getKey());
			SpawnManager.getInstance().bringIntoWorld(object, instanceIndex);
		}
		return object;
	}
}
