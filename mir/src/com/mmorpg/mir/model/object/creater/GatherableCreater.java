package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.GatherableController;
import com.mmorpg.mir.model.gameobjects.Gatherable;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public class GatherableCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource sresource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = sresource.createXY();
		Gatherable gatherable = new Gatherable(identifyManager.getNextIdentify(IdentifyType.MONSTER),
				new GatherableController(), world.createPosition(sresource.getMapId(), instanceIndex, fxy[0], fxy[1],
						sresource.createHeading()));
		gatherable.setBornX(fxy[0]);
		gatherable.setBornY(fxy[1]);
		gatherable.setSpawnKey(sresource.getKey());
		gatherable.setObjectKey(resource.getKey());
		gatherable.setTemplateId(resource.getTemplateId());
		gatherable.setRewardId(resource.getRewardId());
		gatherable.setInterval(resource.getInterval() * 1000);
		gatherable.setHeading(sresource.createHeading());
		return gatherable;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.GATHERABLE;
	}

	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
		Gatherable gatherable = (Gatherable) object;

		gatherable.reset();
	}

}
