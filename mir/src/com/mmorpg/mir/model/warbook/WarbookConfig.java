package com.mmorpg.mir.model.warbook;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.warbook.resource.WarbookItemResource;
import com.mmorpg.mir.model.warbook.resource.WarbookResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class WarbookConfig {

	@Static
	public Storage<Integer, WarbookResource> warBookStorage;

	@Static
	public Storage<String, WarbookItemResource> warBookItemStorage;

	private static WarbookConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static WarbookConfig getInstance() {
		return INSTANCE;
	}

	public WarbookResource getWarbookResourceByGrade(int grade) {
		return warBookStorage.get(grade, true);
	}

	public WarbookItemResource getWarbookItemResourceById(String itemId) {
		return warBookItemStorage.get(itemId, true);
	}

	@Static("WARBOOK:WARBOOK_CRI_CHOOSERGROUP_ID")
	public ConfigValue<String> WARBOOK_CRI_CHOOSERGROUP_ID;

	/** 清楚祝福值的时间间隔（小时） */
	@Static("WARBOOK:CLEAR_NOW_BLESS_INTERVAL")
	private ConfigValue<Integer> CLEAR_NOW_BLESS_INTERVAL;

	public long getClearBlessInterval() {
		return this.CLEAR_NOW_BLESS_INTERVAL.getValue() * DateUtils.MILLIS_PER_HOUR;
	}

	@Static("WARBOOK:WARBOOK_NOTICE_GRADE")
	public ConfigValue<Integer> WARBOOK_NOTICE_GRADE;
}
