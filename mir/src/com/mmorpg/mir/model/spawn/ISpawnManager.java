package com.mmorpg.mir.model.spawn;

import java.util.Collection;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;

public interface ISpawnManager {
	public VisibleObject spawnObject(String spawnKey, int instanceIndex, Object... args);

	VisibleObject creatObject(String spawnKey, int instanceIndex, Object... args);

	VisibleObject spawnObject(SpawnGroupResource spawn, int instanceIndex, Object... args);

	void bringIntoWorld(VisibleObject visibleObject, int instanceIndex);

	void spawnAll();

	void spawnInstance(int mapId, int instanceIndex);

	int spawn(SpawnGroupResource resource, int instanceIndex);

	int spawn(SpawnGroupResource resource, int instanceIndex, int num);

	SpawnGroupResource getSpawn(String key);

	void spawnDropObject(Creature lastAttacker, Player mostDamagePlayer, Npc monster, Collection<Player> owners);

	SpawnGroupResource createSpawnGroupResource(VisibleObject object, String key);
}
