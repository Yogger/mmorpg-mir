package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
@SuppressWarnings("rawtypes")
public class ConfigValueCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, ConfigValue> configValues;

	@Override
	public Class<?> getResourceClass() {
		return ConfigValue.class;
	}

	@Override
	public void check() {
		for (ConfigValue cf : configValues.getAll()) {
			cf.getValue();
		}
	}

}
