package com.mmorpg.mir.model.spawn;

import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;

public interface IRobotManager {
	void spawnRobotAll();

	void spawnRobotInstance(int mapId, int instanceIndex);

	int spawnRobot(SpawnGroupResource resource, int instanceIndex);

	VisibleObject spawnRobotObject(SpawnGroupResource spawn, int instanceIndex, Object... args);
}
