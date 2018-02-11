package com.mmorpg.mir.model.object.creater;

import java.util.Arrays;
import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.SummonLifeStats;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.gameobjects.stats.SummonGameStats;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.MasterFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.WorldPosition;

@Component
public class BigBrotherCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		WorldPosition position = world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0], fxy[1],
				spawnResource.createHeading());
		BigBrother summon = new BigBrother(identifyManager.getNextIdentify(IdentifyType.MONSTER),
				new SummonController(), position, (Player) args[0]);
		summon.setBornX(fxy[0]);
		summon.setBornY(fxy[1]);
		summon.setAi(spawnResource.getAiType().create());
		summon.setGameStats(new SummonGameStats(summon));
		summon.getAi().setOwner(summon);
		summon.getAi().setInterval(500);
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

		List<Stat> stats = Arrays.asList(resource.getStats());
		if (!stats.isEmpty()) {
			summon.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE), stats);
		} else {
			stats = New.arrayList();
			stats.add(new Stat(StatEnum.MAXHP, 100, 0, 0));
			stats.add(new Stat(StatEnum.MAGICAL_ATTACK, 100, 0, 0));
			stats.add(new Stat(StatEnum.PHYSICAL_ATTACK, 100, 0, 0));
			stats.add(new Stat(StatEnum.MAGICAL_DEFENSE, 50, 0, 0));
			stats.add(new Stat(StatEnum.PHYSICAL_DEFENSE, 100, 0, 0));
			stats.add(new Stat(StatEnum.SPEED, 250, 0, 0));
			summon.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE), stats);
		}

		summon.setLifeStats(new SummonLifeStats(summon, summon.getGameStats().getCurrentStat(StatEnum.MAXHP), 0, false));

		summon.setEffectController(new EffectController(summon));

		return summon;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.BIGBROTHER;
	}

}
