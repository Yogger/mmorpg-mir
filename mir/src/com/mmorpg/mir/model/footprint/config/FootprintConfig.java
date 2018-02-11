package com.mmorpg.mir.model.footprint.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.footprint.resource.FootprintResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class FootprintConfig {

	@Static
	private Storage<Integer, FootprintResource> footprintResources;

	/**
	 * 足迹最大激活次数
	 */
	@Static("FOOTPRINT:MAX_ACTIVATE_COUNT")
	public ConfigValue<Integer> MAX_ACTIVATE_COUNT;

	private static FootprintConfig instance;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public FootprintResource getResource(int id) {
		return footprintResources.get(id, true);
	}

	public static FootprintConfig getInstance() {
		return instance;
	}

	public static void setInstance(FootprintConfig instance) {
		FootprintConfig.instance = instance;
	}

	public SimpleScheduler getSimpleScheduler() {
		return simpleScheduler;
	}

	public void setSimpleScheduler(SimpleScheduler simpleScheduler) {
		this.simpleScheduler = simpleScheduler;
	}
}
