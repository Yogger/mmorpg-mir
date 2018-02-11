package com.mmorpg.mir.model.object;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.creater.AbstractObjectCreater;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public final class ObjectManager implements IObjectManager {

	private static final Logger logger = Logger.getLogger(ObjectManager.class);

	@Static
	public Storage<String, ObjectResource> objectResourceStorage;

	private static ObjectManager self;

	public static ObjectManager getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		// TODO 生物模版初始化，如果有可能的话
		self = this;
	}

	public ObjectResource getObjectResource(String key) {
		return objectResourceStorage.get(key, true);
	}

	/**
	 * 根据key创建一个可见生物
	 * 
	 * @param key
	 * @return
	 */
	public VisibleObject createObject(SpawnGroupResource sresource, int instanceIndex, Object... args) {
		ObjectResource resource = objectResourceStorage.get(sresource.getObjectKey(), true);
		AbstractObjectCreater creater = AbstractObjectCreater.getCreater(resource.getObjectType());
		if (creater == null) {
			logger.error(String.format("no any creater found for object type : [%s] exception",
					resource.getObjectType()));
			return null;
		}
		return creater.create(sresource, resource, instanceIndex, args);
	}

	private static final String[] rotbot = new String[] { "protectcopyenemy1", "protectcopyenemy2",
			"protectcopyenemy3", "protectcopyenemy4", "protectcopyenemy5", "protectcopyenemy6", "protectcopyenemy7",
			"protectcopyenemy8", "protectcopyenemy9", "protectcopyFriend1", "protectcopyFriend2", "protectcopyFriend3",
			"protectcopyFriend4" };

	public VisibleObject createRobotObject(SpawnGroupResource sresource, int instanceIndex, Object... args) {
		Random random = MathUtil.getRandom();
		ObjectResource resource = objectResourceStorage.get(rotbot[random.nextInt(rotbot.length)], true);
		AbstractObjectCreater creater = AbstractObjectCreater.getCreater(ObjectType.ROBOT);
		return creater.create(sresource, resource, instanceIndex, random.nextInt(3) + 1);
	}

	public void reliveObject(VisibleObject object, Object... args) {
		ObjectResource resource = objectResourceStorage.get(object.getObjectKey(), true);
		AbstractObjectCreater.getCreater(resource.getObjectType()).relive(resource, object, args);
	}
}
