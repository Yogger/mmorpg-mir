package com.mmorpg.mir.model.seal;

import java.util.Date;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.seal.resource.SealItemResource;
import com.mmorpg.mir.model.seal.resource.SealResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;

@Component
public class SealConfig {
	
	private static SealConfig sealConfig;
	
	public static SealConfig getInstance() {
		return sealConfig;
	}

	@PostConstruct
	void init() {
		sealConfig = this;
	}
	
	@Autowired
	private SimpleScheduler simpleSceduler;
	
	@Static
	public Storage<String, SealItemResource> sealItemResources;
	
	@Static
	public Storage<Integer, SealResource> sealResources;

	public SealResource getResource(int grade) {
		return sealResources.get(grade, true);
	}
	
	public SealItemResource getSealItemResource(String key) {
		return sealItemResources.get(key, true);
	}
	
	@Static("SEAL:SEAL_CRI_CHOOSERGROUP_ID")
	public ConfigValue<String> SEAL_CRI_CHOOSERGROUP_ID;
	
	/** 清楚祝福值的时间间隔（小时） */
	@Static("SEAL:CLEAR_NOW_BLESS_INTERVAL")
	private ConfigValue<Integer> CLEAR_NOW_BLESS_INTERVAL;

	public long getClearBlessInterval() {
		return this.CLEAR_NOW_BLESS_INTERVAL.getValue() * DateUtils.MILLIS_PER_HOUR;
	}

	@Static("SEAL:SEAL_NOTICE_GRADE")
	public ConfigValue<Integer> SEAL_NOTICE_GRADE;
	
	public Future<?> touchTask(ScheduledTask task, Date date) {
		return simpleSceduler.schedule(task, date);
	}
}
