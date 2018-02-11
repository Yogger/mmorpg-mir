package com.mmorpg.mir.model.init;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.formula.Formula;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class InitManager implements IInitManager {

	private static final Logger logger = Logger.getLogger(InitManager.class);
	@Static
	private Storage<String, Formula> formulas;

	@Static
	private Storage<String, ConfigValue> configValues;

	@PostConstruct
	public final void init() {
		long now = System.currentTimeMillis();
		for (Formula formula : formulas.getAll()) {
			formula.compile();
		}
		for (ConfigValue configValue : configValues.getAll()) {
			configValue.getValue();
		}
		logger.error("服务器暖机处理消耗 " + (System.currentTimeMillis() - now) + " ms");
	}

}
