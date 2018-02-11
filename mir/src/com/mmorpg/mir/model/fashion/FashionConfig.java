package com.mmorpg.mir.model.fashion;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.fashion.resource.FashionLevelResource;
import com.mmorpg.mir.model.fashion.resource.FashionResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class FashionConfig {

	@Static
	public Storage<Integer, FashionResource> fashionStorage;

	@Static
	public Storage<Integer, FashionLevelResource> fashionLevelStorage;

	private static FashionConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static FashionConfig getInstance() {
		return INSTANCE;
	}

	/** 最大等级 */
	@Static("FASHION:FASHION_MAX_LEVEL")
	public ConfigValue<Integer> FASHION_MAX_LEVEL;

	/** 国王的时装id */
	@Static("FASHION:KING_FASHIONID")
	public ConfigValue<Integer> KING_FASHIONID;

	/** 皇帝的时装id */
	@Static("FASHION:KING_OF_KING_FASHIONID")
	public ConfigValue<Integer> KING_OF_KING_FASHIONID;

	@Static("FASHION:FASHION_UPGRDE_CONDS")
	private ConfigValue<String[]> FASHION_UPGRDE_CONDS;
	
	@Static("FASHION:LOGIN_KING_MAIL_TITLE")
	public ConfigValue<String> LOGIN_KING_MAIL_TITLE;
	
	@Static("FASHION:LOGIN_KING_MAIL_CONTENT")
	public ConfigValue<String> LOGIN_KING_MAIL_CONTENT;
	
	@Static("FASHION:LOGIN_KING_OF_KING_MAIL_TITLE")
	public ConfigValue<String> LOGIN_KING_OF_KING_MAIL_TITLE;
	
	@Static("FASHION:LOGIN_KING_OF_KING_MAIL_CONTENT")
	public ConfigValue<String> LOGIN_KING_OF_KING_MAIL_CONTENT;

	private CoreConditions upgradeConds;

	public CoreConditions getUpgradeConds() {
		if (upgradeConds == null) {
			upgradeConds = CoreConditionManager.getInstance().getCoreConditions(1, FASHION_UPGRDE_CONDS.getValue());
		}
		return upgradeConds;
	}
}
