package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.SummonLifeStats;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.gameobjects.stats.SummonGameStats;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.MasterFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.WorldPosition;

@Component
public class SummonCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		// int[] fxy = spawnResource.createXY();
		Player master = (Player) args[0];
		WorldPosition position = world.createPosition(master.getMapId(), master.getInstanceId(), master.getX(),
				master.getY(), spawnResource.createHeading());
		Summon summon = new Summon(identifyManager.getNextIdentify(IdentifyType.MONSTER), new SummonController(),
				position, master);
		summon.setBornX(master.getX());
		summon.setBornY(master.getY());
		summon.setAi(spawnResource.getAiType().create());
		summon.setGameStats(new SummonGameStats(summon));
		summon.getAi().setOwner(summon);
		summon.getAi().setInterval(200);
		summon.setSpawnKey(spawnResource.getKey());
		summon.setObjectKey(resource.getKey());
		summon.setTemplateId(resource.getTemplateId());
		summon.setNpcSkillIds(resource.getSkills());
		summon.setSkillSelector(SkillSelector.valueOf(summon, resource.getSkillSelectorItemSamples()));

		summon.setWarnrange(spawnResource.getWarnrange());
		summon.setHomeRange(spawnResource.getHomerange());

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			summon.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			summon.setFollowPolicy(new MasterFollowPolicy(summon));
		}

		// 设置属性
		setStats(summon, resource);

		summon.setLifeStats(new SummonLifeStats(summon, summon.getGameStats().getCurrentStat(StatEnum.MAXHP), 0, false));

		summon.setEffectController(new EffectController(summon));

		return summon;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SUMMON;
	}

}
