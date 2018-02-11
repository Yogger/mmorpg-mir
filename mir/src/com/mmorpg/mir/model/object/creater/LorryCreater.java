package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.LorryController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.LorryLifeStats;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.LorryGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.WorldPosition;

@Component
public class LorryCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		WorldPosition position = world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0], fxy[1],
				spawnResource.createHeading());
		Player player = (Player) args[0];
		Integer color = (Integer) args[1];
		Lorry lorry = new Lorry(identifyManager.getNextIdentify(IdentifyType.MONSTER), new LorryController(), position,
				player, color);
		lorry.setBornX(fxy[0]);
		lorry.setBornY(fxy[1]);
		lorry.setAi(spawnResource.getAiType().create());
		lorry.setGameStats(new LorryGameStats(lorry));
		lorry.getAi().setOwner(lorry);
		lorry.getAi().setInterval(200);
		lorry.setSpawnKey(spawnResource.getKey());
		lorry.setObjectKey(resource.getKey());
		lorry.setTemplateId(resource.getTemplateId());

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			lorry.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
		}

		// 设置属性
		setStats(lorry, resource);

		lorry.setLifeStats(new LorryLifeStats(lorry, lorry.getGameStats().getCurrentStat(StatEnum.MAXHP), 0, false));

		lorry.setEffectController(new EffectController(lorry));

		return lorry;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.LORRY;
	}

}
