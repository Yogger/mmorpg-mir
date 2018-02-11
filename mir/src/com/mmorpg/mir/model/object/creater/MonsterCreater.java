package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.MonsterController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.MonsterLifeStats;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.MonsterGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.RouteFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public final class MonsterCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		Monster monster = new Monster(identifyManager.getNextIdentify(IdentifyType.MONSTER), new MonsterController(),
				world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0], fxy[1],
						spawnResource.createHeading()));
		monster.setBornX(fxy[0]);
		monster.setBornY(fxy[1]);
		monster.setAi(spawnResource.getAiType().create());
		monster.getAi().setInterval(spawnResource.getAiInterval());

		// 设置怪物的一些属性
		monster.setGameStats(new MonsterGameStats(monster));
		monster.getAi().setOwner(monster);
		monster.setSpawnKey(spawnResource.getKey());
		monster.setObjectKey(resource.getKey());
		monster.setTemplateId(resource.getTemplateId());
		monster.setNpcSkillIds(resource.getSkills());
		monster.setWarnrange(spawnResource.getWarnrange());
		monster.setHomeRange(spawnResource.getHomerange());
		monster.setSkillSelector(SkillSelector.valueOf(monster, resource.getSkillSelectorItemSamples()));
		monster.setBornHeading(monster.getHeading());
		monster.setExpReduction(resource.isExpReduction());

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			monster.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			monster.setFollowPolicy(new RouteFollowPolicy(monster));
		}
		// 设置属性
		setStats(monster, resource);

		monster.setLifeStats(new MonsterLifeStats(monster, monster.getGameStats().getCurrentStat(StatEnum.MAXHP),
				monster.getGameStats().getCurrentStat(StatEnum.MAXMP), false));

		monster.setEffectController(new EffectController(monster));

		return monster;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.MONSTER;
	}

	@Override
	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
		Monster monster = (Monster) object;
		// 设置属性
		setStats(monster, resource);

		monster.getLifeStats().setCurrentHpPercent(100);

		// 这里需要重置一下路线
		if (monster.hasRouteStep())
			monster.getRouteRoad().reset();

	}

}
