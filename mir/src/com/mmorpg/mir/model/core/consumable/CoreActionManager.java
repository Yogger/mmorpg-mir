package com.mmorpg.mir.model.core.consumable;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.mmorpg.mir.model.core.context.KeyContext;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CoreActionManager {

	@Static
	private Storage<String, CoreActionResource> coreActionResources;

	private static CoreActionManager self;

	public static CoreActionManager getInstance() {
		return self;
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		self = this;
	}

	public CoreActions getCoreActions(int size, CoreActionResource... resources) {
		CoreActions coreActions = new CoreActions();
		if (resources != null && resources.length > 0) {
			AbstractCoreAction[] actions = new AbstractCoreAction[resources.length];
			for (int i = 0, j = resources.length; i < j; i++) {
				CoreActionResource resource = resources[i];
				actions[i] = resource.getType().create();
				actions[i].init(resource);
				actions[i].calculate(size);
			}
			coreActions.addActions(actions);
		}
		return coreActions;
	}

	public CoreActions getCoreActions(String... keys) {
		return getCoreActions(1, keys);
	}

	public CoreActions getCoreActions(int size, String... keys) {
		CoreActions coreActions = new CoreActions();
		if (size <= 0) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (keys != null && keys.length > 0) {
			AbstractCoreAction[] actions = new AbstractCoreAction[keys.length];
			for (int i = 0, j = keys.length; i < j; i++) {
				CoreActionResource resource = coreActionResources.get(keys[i], true);
				actions[i] = resource.getType().create();
				actions[i].init(resource);
				actions[i].calculate(size);
			}
			coreActions.addActions(actions);
		}
		return coreActions;
	}

	public CoreActions getCoreActions(KeyContext... contexts) {
		CoreActions coreActions = new CoreActions();
		if (contexts != null && contexts.length > 0) {
			AbstractCoreAction[] actions = new AbstractCoreAction[contexts.length];
			for (int i = 0, j = contexts.length; i < j; i++) {
				CoreActionResource resource = coreActionResources.get(contexts[i].getKey(), true);
				actions[i] = resource.getType().create();
				actions[i].init(resource);
				actions[i].value = resource.calculateValue(contexts[i].getContext());
				actions[i].calculate(contexts[i].getMount());
			}
			coreActions.addActions(actions);
		}
		return coreActions;
	}
}
