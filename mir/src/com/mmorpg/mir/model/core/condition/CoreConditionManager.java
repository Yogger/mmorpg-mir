package com.mmorpg.mir.model.core.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.context.KeyContext;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CoreConditionManager {

	@Static
	private Storage<String, CoreConditionResource> coreConditionResource;

	private static CoreConditionManager self;

	public static CoreConditionManager getInstance() {
		return self;
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		self = this;
	}

	public CoreConditions getCoreConditions(int size, String... keys) {
		CoreConditions coreConditions = new CoreConditions();
		if (keys != null && keys.length > 0) {
			AbstractCoreCondition[] conditions = new AbstractCoreCondition[keys.length];
			for (int i = 0, j = keys.length; i < j; i++) {
				CoreConditionResource resource = coreConditionResource.get(keys[i], true);
				conditions[i] = resource.getType().create();
				try {
					conditions[i].init(resource);
				} catch (Exception e) {
					System.out.println(keys[i]);
				}
				conditions[i].calculate(size);
			}
			coreConditions.addConditions(conditions);
		}
		return coreConditions;
	}

	public CoreConditionResource[] getCoreConditionResources(String... keys) {
		List<CoreConditionResource> resources = new ArrayList<CoreConditionResource>();
		if (keys != null && keys.length > 0) {
			for (int i = 0, j = keys.length; i < j; i++) {
				CoreConditionResource r = coreConditionResource.get(keys[i], true);
				resources.add(r);
			}
		}
		int size = resources.size();
		return resources.toArray(new CoreConditionResource[size]);
	}

	public CoreConditions getCoreConditions(KeyContext... contexts) {
		CoreConditions coreConditions = new CoreConditions();
		if (contexts != null && contexts.length > 0) {
			AbstractCoreCondition[] conditions = new AbstractCoreCondition[contexts.length];
			for (int i = 0, j = contexts.length; i < j; i++) {
				CoreConditionResource resource = coreConditionResource.get(contexts[i].getKey(), true);
				conditions[i] = resource.getType().create();
				conditions[i].init(resource);
				conditions[i].calculate(contexts[i].getMount());
			}
			coreConditions.addConditions(conditions);
		}
		return coreConditions;
	}

	public CoreConditions getCoreConditions(CoreConditionResource... resources) {
		return getCoreConditions(1, resources);
	}

	public CoreConditions getCoreConditions(int size, CoreConditionResource... resources) {
		// 按道理说不应该传一个空的条件，不做空的验证是希望错误早一点报错，而不是一直通过验证
		CoreConditions coreConditions = new CoreConditions();
		AbstractCoreCondition[] conditions = new AbstractCoreCondition[resources.length];
		for (int i = 0; i < resources.length; i++) {
			conditions[i] = resources[i].getType().create();
			conditions[i].init(resources[i]);
			// conditions[i].value = resources[i].calculateValue(null);
			conditions[i].calculate(size);
		}
		coreConditions.addConditions(conditions);
		return coreConditions;
	}
	
	public boolean isPass(Object object, String[] limitIds, Map<String, Object> limitCtx, boolean throwException) {
		CoreConditions coreConditions = getCoreConditions(1, limitIds);
		return coreConditions.verify(object);
	}

	public Storage<String, CoreConditionResource> getCoreConditionResource() {
		return coreConditionResource;
	}

	public static CoreConditions getCoreConditionResources(int size, CoreConditionResource[] resources) {
		CoreConditions coreConditions = new CoreConditions();
		AbstractCoreCondition[] conditions = new AbstractCoreCondition[resources.length];
		for (int i = 0; i < resources.length; i++) {
			conditions[i] = resources[i].getType().create();
			conditions[i].init(resources[i]);
			// conditions[i].value = resources[i].calculateValue(null);
			conditions[i].calculate(size);
		}
		coreConditions.addConditions(conditions);
		return coreConditions;
	}
}
