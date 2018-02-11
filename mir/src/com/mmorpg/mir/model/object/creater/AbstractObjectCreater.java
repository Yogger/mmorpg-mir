package com.mmorpg.mir.model.object.creater;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.dynamicstats.StatsResource;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

public abstract class AbstractObjectCreater {

	private static final Map<ObjectType, AbstractObjectCreater> createrMap = new HashMap<ObjectType, AbstractObjectCreater>();

	@Autowired
	protected IdentifyManager identifyManager;

	@Static
	protected Storage<Integer, StatsResource> statsResources;

	@Autowired
	protected World world;

	@PostConstruct
	protected void init() {
		createrMap.put(getObjectType(), this);
	}

	public abstract VisibleObject create(SpawnGroupResource sresource, ObjectResource resource, int instanceIndex,
			Object... args);

	public void relive(ObjectResource resource, VisibleObject object, Object... args) {

	}

	protected void setStats(Creature creature, ObjectResource resource) {
		creature.getGameStats().clear();
		if (resource.isDynamicStats()) {
			int worldLevel = Math.max(resource.getMinWorldLevel(), World.getInstance().getWorldLevel());
			if (resource.getMaxWorldLevel() != 0) {
				worldLevel = Math.min(resource.getMaxWorldLevel(), worldLevel);
			}
			creature.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
					resource.getStats(statsResources.get(worldLevel, true)));
			creature.setLevel(worldLevel);
		} else {
			List<Stat> stats = Arrays.asList(resource.getStats());
			if (!stats.isEmpty()) {
				creature.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
						stats);
				creature.setLevel(resource.getLevel());
			} else {
				throw new RuntimeException(String.format("ObjectResource id[%s]属性为空", resource.getKey()));
			}
		}
	}

	public void setStats(Creature creature, ObjectResource resource, int level) {
		creature.getGameStats().clear();
		if (resource.isDynamicStats()) {
			creature.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
					resource.getStats(statsResources.get(level, true)));
			creature.setLevel(level);
		} else {
			List<Stat> stats = Arrays.asList(resource.getStats());
			if (!stats.isEmpty()) {
				creature.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
						stats);
				creature.setLevel(resource.getLevel());
			} else {
				throw new RuntimeException(String.format("ObjectResource id[%s]属性为空", resource.getKey()));
			}
		}
	}

	public abstract ObjectType getObjectType();

	public static AbstractObjectCreater getCreater(ObjectType type) {
		return createrMap.get(type);
	}

	public Storage<Integer, StatsResource> getStatsResources() {
		return statsResources;
	}

}
