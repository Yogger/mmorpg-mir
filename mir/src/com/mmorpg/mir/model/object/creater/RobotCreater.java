package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.AIType;
import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.CountryNpcLifeStats;
import com.mmorpg.mir.model.gameobjects.Robot;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.CountryNpcGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.CountryNpcFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public class RobotCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		int country = spawnResource.getCountry();
		if (args != null && args.length >= 1 && args[0] instanceof Integer) {
			country = (Integer) args[0];
		}
		Robot countryNpc = new Robot(identifyManager.getNextIdentify(IdentifyType.COUNTRY_NPC),
				new CountryNpcController(), world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0],
						fxy[1], spawnResource.createHeading()), country);
		countryNpc.setBornX(fxy[0]);
		countryNpc.setBornY(fxy[1]);
		countryNpc.setBornHeading(spawnResource.getHeading() == null ? 0 : Byte.valueOf(spawnResource.getHeading()));
		countryNpc.setAi(AIType.ROBOT.create());
		countryNpc.setGameStats(new CountryNpcGameStats(countryNpc));
		countryNpc.getAi().setOwner(countryNpc);
		countryNpc.setSpawnKey(spawnResource.getKey());
		countryNpc.setObjectKey(resource.getKey());
		countryNpc.setTemplateId(resource.getTemplateId());
		countryNpc.setNpcSkillIds(resource.getSkills());
		countryNpc.setSkillSelector(SkillSelector.valueOf(countryNpc, resource.getSkillSelectorItemSamples()));
		countryNpc.setWarnrange(20);
		countryNpc.setHomeRange(40);

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			countryNpc.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			countryNpc.setFollowPolicy(new CountryNpcFollowPolicy(countryNpc));
		}

		// 设置属性
		setStats(countryNpc, resource);

		countryNpc.setLifeStats(new CountryNpcLifeStats(countryNpc, countryNpc.getGameStats().getCurrentStat(
				StatEnum.MAXHP), countryNpc.getGameStats().getCurrentStat(StatEnum.MAXMP), false));

		countryNpc.setEffectController(new EffectController(countryNpc));

		return countryNpc;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.ROBOT;
	}

	@Override
	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
		Robot countryNpc = (Robot) object;

		// 设置属性
		setStats(countryNpc, resource);

		countryNpc.getLifeStats().setCurrentHpPercent(100);

		// 这里需要重置一下路线
		if (countryNpc.hasRouteStep())
			countryNpc.getRouteRoad().reset();

	}
}
