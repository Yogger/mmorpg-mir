package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.BossLifeStats;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.MonsterGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.RouteFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapCountry;

@Component
public final class BossCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		BossController bossController = null;
		if (args != null && args.length >= 1 && args[0] instanceof BossController) {
			bossController = (BossController) args[0];
		} else {
			bossController = new BossController();
		}
		WorldPosition wp = null;
		try {
			wp = world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0], fxy[1],
					spawnResource.createHeading());
		} catch (Exception e) {
			new RuntimeException(String.format("Boss[%s]初始化错误", spawnResource.getKey()), e);
		}
		Boss monster = new Boss(identifyManager.getNextIdentify(IdentifyType.BOSS), bossController, wp);
		monster.setBornX(fxy[0]);
		monster.setBornY(fxy[1]);
		monster.setAi(spawnResource.getAiType().create());
		monster.setGameStats(new MonsterGameStats(monster));
		monster.getAi().setOwner(monster);
		monster.getAi().setInterval(200);
		monster.setSpawnKey(spawnResource.getKey());
		monster.setObjectKey(resource.getKey());
		monster.setTemplateId(resource.getTemplateId());
		monster.setNpcSkillIds(resource.getSkills());
		monster.setSkillSelector(SkillSelector.valueOf(monster, resource.getSkillSelectorItemSamples()));
		monster.setWarnrange(spawnResource.getWarnrange());
		monster.setHomeRange(spawnResource.getHomerange());
		monster.setCountry(MapCountry.valueOf(spawnResource.getCountry()));
		monster.setHeading(spawnResource.createHeading());
		monster.setBornHeading(spawnResource.getHeading() == null ? 0 : Byte.valueOf(spawnResource.getHeading()));

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			monster.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			monster.setFollowPolicy(new RouteFollowPolicy(monster));
		}

		// 设置属性
		setStats(monster, resource);

		monster.setLifeStats(new BossLifeStats(monster, monster.getGameStats().getCurrentStat(StatEnum.MAXHP), monster
				.getGameStats().getCurrentStat(StatEnum.MAXMP), false));

		monster.setEffectController(new EffectController(monster));

		return monster;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.BOSS;
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
