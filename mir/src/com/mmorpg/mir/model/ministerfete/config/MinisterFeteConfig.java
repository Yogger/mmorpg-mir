package com.mmorpg.mir.model.ministerfete.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Static;

@Component
public class MinisterFeteConfig {
	
	private static MinisterFeteConfig INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static MinisterFeteConfig getInstance() {
		return INSTANCE;
	}
	
	@Static("XGJT:NOTICE_TIME")
	public ConfigValue<String> NOTICE_TIME;
	
	@Static("XGJT:OPEN_TIME")
	public ConfigValue<String[]> OPEN_TIME;
	
	@Static("XGJT:END_TIME")
	public ConfigValue<String[]> END_TIME;
	
	@Static("XGJT:MAPID")
	public ConfigValue<Integer> MAPID;
	
	@Static("XGJT:BOSSID")
	public ConfigValue<String> BOSSID;
	
	@Static("XGJT:XGJT_DAMAGE_FACTOR_CHOOSERGROUP")
	public ConfigValue<String> XGJT_DAMAGE_FACTOR_CHOOSERGROUP;
	
	@Static("XGJT:XGJT_DAMAGE_REWARD_CHOOSERGROUP")
	public ConfigValue<String> XGJT_DAMAGE_REWARD_CHOOSERGROUP;
	
	@Static("XGJT:XGJT_LAST_ATTACK_REWARD_CHOOSERGROUP")
	public ConfigValue<String> XGJT_LAST_ATTACK_REWARD_CHOOSERGROUP;
	
	@Static("XGJT:XGJT_ROLL_REWARD_CHOOSERGROUP")
	public ConfigValue<String> XGJT_ROLL_REWARD_CHOOSERGROUP;
	
	@Static("XGJT:HPLESSTHAN_LIGHT")
	public ConfigValue<Integer[]> HPLESSTHAN_LIGHT;
	
	@Static("XGJT:ROLL_TIME")
	public ConfigValue<Integer> ROLL_TIME;
	
	@Static("XGJT:BOSS_STUN_SKILL")
	public ConfigValue<Integer> BOSS_STUN_SKILL;
	
	@Static("XGJT:MAIL_TITLE_CHOOSERGROUP")
	public ConfigValue<String> MAIL_TITLE_CHOOSERGROUP;
	
	@Static("XGJT:MAIL_CONTENT_CHOOSERGROUP")
	public ConfigValue<String> MAIL_CONTENT_CHOOSERGROUP;
	
	@Static("XGJT:LAST_ATTACK_MAIL_TITLE")
	public ConfigValue<String> LAST_ATTACK_MAIL_TITLE;
	
	@Static("XGJT:LAST_ATTACK_MAIL_CONTENT")
	public ConfigValue<String> LAST_ATTACK_MAIL_CONTENT;
	
	@Static("XGJT:ENTER_COND_ID")
	public ConfigValue<String[]> ENTER_COND_ID;
	
	@Static("XGJT:BOSS_SKILL_CD")
	public ConfigValue<Integer> BOSS_SKILL_CD;
	
	@Static("XGJT:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;
	
	@Static("XGJT:RANDOM_MAIL_TITLE")
	public ConfigValue<String> RANDOM_MAIL_TITLE;
	
	@Static("XGJT:RANDOM_MAIL_CONTENT")
	public ConfigValue<String> RANDOM_MAIL_CONTENT;
	
	@Static("XGJT:UPGRADE_POINT_TIME")
	public ConfigValue<Integer> UPGRADE_POINT_TIME;
	
	private CoreConditions enterCondition;
	
	public CoreConditions getEnterCondition() {
		if (enterCondition == null) {
			enterCondition = CoreConditionManager.getInstance().getCoreConditions(1, ENTER_COND_ID.getValue());
		}
		return enterCondition;
	}
	
	public boolean isTooEarlyDead(long startTime) {
		if (startTime == 0) {
			return false;
		}
		return System.currentTimeMillis() < (startTime + (1000L * UPGRADE_POINT_TIME.getValue()));
	}
	
}

