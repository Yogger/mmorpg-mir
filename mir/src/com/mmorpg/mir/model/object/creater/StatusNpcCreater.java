package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.MonsterLifeStats;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public class StatusNpcCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = spawnResource.createXY();
		StatusNpcController statusNpcController = null;
		if (args != null && args.length >= 1 && args[0] instanceof StatusNpcController) {
			statusNpcController = (StatusNpcController) args[0];
		} else {
			statusNpcController = new StatusNpcController();
		}
		StatusNpc statusNpc = new StatusNpc(identifyManager.getNextIdentify(IdentifyType.MONSTER), statusNpcController,
				world.createPosition(spawnResource.getMapId(), instanceIndex, fxy[0], fxy[1],
						spawnResource.createHeading()));
		statusNpc.setBornX(fxy[0]);
		statusNpc.setBornY(fxy[1]);
		statusNpc.setAi(spawnResource.getAiType().create());
		// TODO 设置怪物的一些属性
		statusNpc.getAi().setOwner(statusNpc);
		statusNpc.setSpawnKey(spawnResource.getKey());
		statusNpc.setObjectKey(resource.getKey());
		statusNpc.setTemplateId(resource.getTemplateId());
		statusNpc.setNpcSkillIds(resource.getSkills());
		statusNpc.setSkillSelector(SkillSelector.valueOf(statusNpc, resource.getSkillSelectorItemSamples()));
		statusNpc.setWarnrange(spawnResource.getWarnrange());
		statusNpc.setHomeRange(spawnResource.getHomerange());
		statusNpc.setEffectController(new EffectController(statusNpc));
		statusNpc.setLifeStats(new MonsterLifeStats(statusNpc, 100, 100, false));
		statusNpc.setBornHeading(statusNpc.getHeading());

		return statusNpc;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.STATUS_NPC;
	}

}
