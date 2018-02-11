package com.mmorpg.mir.model.object.creater;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.SupervisorController;
import com.mmorpg.mir.model.gameobjects.Supervisor;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public class SupervisorCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource sresource, ObjectResource resource, int instanceIndex,
			Object... args) {
		int[] fxy = sresource.createXY();
		Supervisor supervisor = new Supervisor(identifyManager.getNextIdentify(IdentifyType.MONSTER),
				new SupervisorController(), world.createPosition(sresource.getMapId(), instanceIndex, fxy[0], fxy[1],
						sresource.createHeading()));
		supervisor.setBornX(fxy[0]);
		supervisor.setBornY(fxy[1]);
		supervisor.setSpawnKey(sresource.getKey());
		supervisor.setObjectKey(resource.getKey());
		supervisor.setTemplateId(resource.getTemplateId());
		supervisor.setHeading(sresource.createHeading());
		return supervisor;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SUPERVISOR;
	}

	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
	}

}
