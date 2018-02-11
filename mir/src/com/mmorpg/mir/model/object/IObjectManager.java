package com.mmorpg.mir.model.object;

import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;

public interface IObjectManager {
	ObjectResource getObjectResource(String key);

	VisibleObject createObject(SpawnGroupResource sresource, int instanceIndex, Object... args);

	VisibleObject createRobotObject(SpawnGroupResource sresource, int instanceIndex, Object... args);

	void reliveObject(VisibleObject object, Object... args);
}
