package com.mmorpg.mir.model.beauty;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.beauty.resource.BeautyGirlItemResource;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class BeautyGirlConfig {

	@Static
	public Storage<String, BeautyGirlResource> beautyGirlStorage;

	@Static
	public Storage<String, BeautyGirlItemResource> beautyGirlItemStorage;

	private static BeautyGirlConfig instance;

	@PostConstruct
	void init() {
		instance = this;
	}

	public static BeautyGirlConfig getInstance() {
		return instance;
	}

	/** 出战cd（单位秒） */
	@Static("BEAUTY:FIGHT_CD_TIME")
	public ConfigValue<Integer> FIGHT_CD_TIME;

	@Static("BEAUTY:BEAUTY_BORN_X")
	public ConfigValue<Integer> BEAUTY_BORN_X;

	@Static("BEAUTY:BEAUTY_BORN_Y")
	public ConfigValue<Integer> BEAUTY_BORN_Y;

	/** 美人距离主人传送的距离 */
	@Static("BEAUTY:SERVANT_TRANSPORT_DISTANCE")
	public ConfigValue<Integer> SERVANT_TRANSPORT_DISTANCE;

}
