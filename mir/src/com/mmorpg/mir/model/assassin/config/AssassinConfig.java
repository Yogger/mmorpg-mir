package com.mmorpg.mir.model.assassin.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.utils.MathUtil;
import com.windforce.common.resource.anno.Static;

@Component
public class AssassinConfig {
	
	private static AssassinConfig INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static AssassinConfig getInstance() {
		return INSTANCE;
	}
	
	@Static("JKCQ:NOTICE_TIME")
	public ConfigValue<String> NOTICE_TIME;
	
	@Static("JKCQ:OPEN_TIME")
	public ConfigValue<String[]> OPEN_TIME;
	
	@Static("JKCQ:END_TIME")
	public ConfigValue<String[]> END_TIME;
	
	@Static("JKCQ:MAPID")
	public ConfigValue<Integer> MAPID;
	
	@Static("JKCQ:BOSSID")
	public ConfigValue<String> BOSSID;
	
	@Static("JKCQ:JKCQ_DAMAGE_FACTOR_CHOOSERGROUP")
	public ConfigValue<String> JKCQ_DAMAGE_FACTOR_CHOOSERGROUP;
	
	@Static("JKCQ:JKCQ_DAMAGE_REWARD_CHOOSERGROUP")
	public ConfigValue<String> JKCQ_DAMAGE_REWARD_CHOOSERGROUP;
	
	@Static("JKCQ:JKCQ_LAST_ATTACK_REWARD_CHOOSERGROUP")
	public ConfigValue<String> JKCQ_LAST_ATTACK_REWARD_CHOOSERGROUP;
	
	@Static("JKCQ:JKCQ_ROLL_REWARD_CHOOSERGROUP")
	public ConfigValue<String> JKCQ_ROLL_REWARD_CHOOSERGROUP;
	
	@Static("JKCQ:HPLESSTHAN_LIGHT")
	public ConfigValue<Integer[]> HPLESSTHAN_LIGHT;
	
	@Static("JKCQ:ROLL_TIME")
	public ConfigValue<Integer> ROLL_TIME;
	
	@Static("JKCQ:BOSS_STUN_SKILL")
	public ConfigValue<Integer> BOSS_STUN_SKILL;
	
	@Static("JKCQ:MAIL_TITLE_CHOOSERGROUP")
	public ConfigValue<String> MAIL_TITLE_CHOOSERGROUP;
	
	@Static("JKCQ:MAIL_CONTENT_CHOOSERGROUP")
	public ConfigValue<String> MAIL_CONTENT_CHOOSERGROUP;
	
	@Static("JKCQ:LAST_ATTACK_MAIL_TITLE")
	public ConfigValue<String> LAST_ATTACK_MAIL_TITLE;
	
	@Static("JKCQ:LAST_ATTACK_MAIL_CONTENT")
	public ConfigValue<String> LAST_ATTACK_MAIL_CONTENT;
	
	@Static("JKCQ:ENTER_COND_ID")
	public ConfigValue<String[]> ENTER_COND_ID;
	
	@Static("JKCQ:BOSS_SKILL_CD")
	public ConfigValue<Integer> BOSS_SKILL_CD;
	
	@Static("JKCQ:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;
	
	@Static("JKCQ:RANDOM_MAIL_TITLE")
	public ConfigValue<String> RANDOM_MAIL_TITLE;
	
	@Static("JKCQ:RANDOM_MAIL_CONTENT")
	public ConfigValue<String> RANDOM_MAIL_CONTENT;
	
	@Static("JKCQ:UPGRADE_POINT_TIME")
	public ConfigValue<Integer> UPGRADE_POINT_TIME;
	
	@Static("JKCQ:START_POINT")
	public ConfigValue<Integer[]> START_POINT;
	
	@Static("JKCQ:END_POINT")
	public ConfigValue<Integer[]> END_POINT;
	
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
	
	private int getDistance() {
		return (int) Math.abs(Math.floor(MathUtil.getDistance(START_POINT.getValue()[0], START_POINT.getValue()[1], 
				END_POINT.getValue()[0], END_POINT.getValue()[1])));
	}
	
	public int getLeftRoad(int x, int y) {
		int gone = (int) Math.abs(Math.floor(MathUtil.getDistance(START_POINT.getValue()[0], START_POINT.getValue()[1], 
					x, y)));
		return 10000 - (gone * 10000 / getDistance()); 
	}
}

