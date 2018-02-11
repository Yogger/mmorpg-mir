package com.mmorpg.mir.model.countrycopy.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class CountryCopyConfig {

	private static CountryCopyConfig self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static CountryCopyConfig getInstance() {
		return self;
	}

	/** 国家副本boss的spawId，ObjectType类型为BOSS */
	@Static("COUNTRYCOPY:BOSS_SPAW")
	public ConfigValue<String> BOSS_SPAW;

	/** 国家副本地图。ps：地图属性不用配置成副本。这样重登以后自然就会不出去了 */
	@Static("COUNTRYCOPY:MAPID")
	public ConfigValue<Integer> MAPID;

	/** 普通玩家每天进入次数 */
	@Static("COUNTRYCOPY:ENTER_COUNT")
	public ConfigValue<Integer> ENTER_COUNT;

	/** 报名等级 */
	@Static("COUNTRYCOPY:ENROLLE_LEVEL")
	public ConfigValue<Integer> ENROLLE_LEVEL;

	/** 进入等级 */
	@Static("COUNTRYCOPY:ENTER_LEVEL")
	public ConfigValue<Integer> ENTER_LEVEL;
	
	@Static("COUNTRYCOPY:ENCOURAGE_LEVEL")
	public ConfigValue<String[]> ENCOURAGE_COND;

	/** 国家副本可以被助威的最大次数 */
	@Static("COUNTRYCOPY:ENCOURAGE_MAX")
	public ConfigValue<Integer> ENCOURAGE_MAX;

	/** 储君进入CD，分钟 */
	@Static("COUNTRYCOPY:RESERVEKING_ENTER_CD")
	public ConfigValue<Integer> RESERVEKING_ENTER_CD;

	/** 普通玩家进入CD，分钟 */
	@Static("COUNTRYCOPY:NORMAL_ENTER_CD")
	public ConfigValue<Integer> NORMAL_ENTER_CD;
	
	public long getPlayerEnterCountryCopyCD(Player player) {
		boolean isReserveKing = player.getCountry().getReserveKing().isReserveKing(player.getObjectId());
		if (isReserveKing) {
			return RESERVEKING_ENTER_CD.getValue() * DateUtils.MILLIS_PER_MINUTE;
		}
		return NORMAL_ENTER_CD.getValue() * DateUtils.MILLIS_PER_MINUTE;
	}
	
	@Static("COUNTRYCOPY:ENTER_COND")
	public ConfigValue<String[]> ENTER_COND;
	
	private CoreConditions enterCountryCopyConditions;
	
	public CoreConditions getEnterCountryCopyConditions() {
		if (enterCountryCopyConditions == null) {
			enterCountryCopyConditions = CoreConditionManager.getInstance().getCoreConditions(1, ENTER_COND.getValue());
		}
		return enterCountryCopyConditions;
	}

	/** 报名以后开始时间，分钟 */
	@Static("COUNTRYCOPY:ENROLLE_START_TIME")
	public ConfigValue<Integer> ENROLLE_START_TIME;

	@Static("COUNTRYCOPY:NPC_SPAWS")
	public ConfigValue<String[]> NPC_SPAWS;

	/** 进入场景出生坐标 配置格式[22,22] */
	@Static("COUNTRYCOPY:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;
	
	@Static("COUNTRYCOPY:STARTED_BORN_POSITION")
	public ConfigValue<Integer[]> STARTED_BORN_POSITION;
	
	@Static("COUNTRYCOPY:STARTED_POISTION_TIME")
	public ConfigValue<Integer> STARTED_POISTION_TIME;

	/** 助威技能Id数组 */
	@Static("COUNTRYCOPY:ENCOURAGE_SKILLIDS")
	public ConfigValue<Integer[]> ENCOURAGE_SKILLIDS;
	
	@Static("COUNTRYCOPY:ENROLL_MAXSIZE_AUTO_START")
	public ConfigValue<Integer> ENROLL_MAXSIZE_AUTO_START;
	
	@Static("COUNTRYCOPY:APPLY_ENCOURAGE_NOTICE_I18N")
	public ConfigValue<String> APPLY_ENCOURAGE_NOTICE_I18N;
	
	@Static("COUNTRYCOPY:APPLY_ENCOURAGE_NOTICE_CHANNEL")
	public ConfigValue<Integer> APPLY_ENCOURAGE_NOTICE_CHANNEL;
	
	@Static("COUNTRYCOPY:ENCOURAGE_REWARD_COUNT")
	public ConfigValue<Integer> ENCOURAGE_REWARD_COUNT;

	@Static("COUNTRYCOPY:ENCOURAGE_REWARD_CHOOSER_GROUP")
	public ConfigValue<String> ENCOURAGE_REWARD_CHOOSER_GROUP;
	
	@Static("COUNTRYCOPY:FINISH_REWARD_CHOOSER_GROUP")
	public ConfigValue<String> FINISH_REWARD_CHOOSER_GROUP;
	
	@Static("COUNTRYCOPY:ENCOURAGE_REWARD_ADDITION")
	public ConfigValue<Double[]> ENCOURAGE_REWARD_ADDITION;
	
	@Static("COUNTRYCOPY:APPLY_ENCOURAGE_CD")
	public ConfigValue<Integer> APPLY_ENCOURAGE_CD;
	
	@Static("COUNTRYCOPY:OPEN_NOTICE_RECIEVE_COND")
	public ConfigValue<String[]> OPEN_NOTICE_RECIEVE_COND;
	
	@Static("COUNTRYCOPY:ENCOURAGE_NOTICE_RECIEVE_COND")
	public ConfigValue<String[]> ENCOURAGE_NOTICE_RECIEVE_COND;
	
	@Static("COUNTRYCOPY:RESERVEKING_ADD_COUNT")
	public ConfigValue<Integer> RESERVEKING_ADD_COUNT;
	
	private CoreConditions openNoticeRecieveCond;
	
	private CoreConditions encourageNoticeRecieveCond;
	
	private CoreConditions encourageCond;
	
	public CoreConditions getEncourageCond() {
		if (encourageCond == null) {
			encourageCond = CoreConditionManager.getInstance().getCoreConditions(1, ENCOURAGE_COND.getValue());
		}
		return encourageCond;
	}
	
	public CoreConditions getNoticeRecieveCond() {
		if (openNoticeRecieveCond == null) {
			openNoticeRecieveCond = CoreConditionManager.getInstance().getCoreConditions(1, OPEN_NOTICE_RECIEVE_COND.getValue());
		}
		return openNoticeRecieveCond;
	}
	
	public CoreConditions getEncourageRecieveCond() {
		if (encourageNoticeRecieveCond == null) {
			encourageNoticeRecieveCond = CoreConditionManager.getInstance().getCoreConditions(1, ENCOURAGE_NOTICE_RECIEVE_COND.getValue());
		}
		return encourageNoticeRecieveCond;
	}
	
	public int getDailyMaxenterCount() {
		if (OpenActiveConfig.getInstance().getCountryCopyActiveConds().verify(null)) {
			return ENTER_COUNT.getValue() + OpenActiveConfig.getInstance().COUNTRYCOPY_OPENACTIVE_ADDCOUNT.getValue();
		} else {
			return ENTER_COUNT.getValue();
		}
	}
	
	@Static("TECHNOLOGYCOPY:TECH_COPY_MAP_ID")
	public ConfigValue<Integer> TECH_COPY_MAP_ID;
	
	@Static("TECHNOLOGYCOPY:BOSS_STUN_SKILL_ID")
	public ConfigValue<Integer> BOSS_STUN_SKIILL_ID;
	
	@Static("TECHNOLOGYCOPY:TECH_COPY_START_COND")
	public ConfigValue<String[]> TECH_COPY_START_COND;
	
	@Static("TECHNOLOGYCOPY:TECH_BOSS_SPAWNKEY")
	public ConfigValue<String> TECH_BOSS_SPAWNKEY;
	
	@Static("TECHNOLOGYCOPY:TECH_MONSTERS_SPAWN_CHOOSER_ID")
	public ConfigValue<String> TECH_MONSTERS_SPAWN_CHOOSER_ID;

	@Static("TECHNOLOGYCOPY:TECH_START_CRON_TIME")
	public ConfigValue<String> TECH_START_CRON_TIME;
	
	@Static("TECHNOLOGYCOPY:TECH_END_CRON_TIME")
	public ConfigValue<String> TECH_END_CRON_TIME;
	
	@Static("TECHNOLOGYCOPY:TECH_START_NOTICE_CRON_TIME")
	public ConfigValue<String> TECH_START_NOTICE_CRON_TIME;
	
	/** 进入场景出生坐标 配置格式[22,22] */
	@Static("TECHNOLOGYCOPY:TECH_BORN_POSITION")
	public ConfigValue<Integer[]> TECH_BORN_POSITION;
	
	@Static("TECHNOLOGYCOPY:TECH_BORN_ENTER_COND")
	public ConfigValue<String[]> TECH_BORN_ENTER_COND;
	
	@Static("TECHNOLOGYCOPY:KILL_MONSTER_GAIN_SKILL")
	public ConfigValue<Map<String, Integer>> KILL_MONSTER_GAIN_SKILL;
	
	@Static("TECHNOLOGYCOPY:TECH_DAMAGE_FACTOR_CHOOSERGROUP")
	public ConfigValue<String> TECH_DAMAGE_FACTOR_CHOOSERGROUP;
	
	@Static("TECHNOLOGYCOPY:TECH_DAMAGE_REWARD_CHOOSERGROUP")
	public ConfigValue<String> TECH_DAMAGE_REWARD_CHOOSERGROUP;
	
	@Static("TECHNOLOGYCOPY:TECH_LAST_ATTACK_REWARD_CHOOSERGROUP")
	public ConfigValue<String> TECH_LAST_ATTACK_REWARD_CHOOSERGROUP;
	
	@Static("TECHNOLOGYCOPY:TECH_DAMAGE_MAIL_TITLE")
	public ConfigValue<String> TECH_DAMAGE_MAIL_TITLE;
	
	@Static("TECHNOLOGYCOPY:TECH_DAMAGE_MAIL_CONTENT")
	public ConfigValue<String> TECH_DAMAGE_MAIL_CONTENT;
	
	@Static("TECHNOLOGYCOPY:TECH_LAST_ATTACK_MAIL_TITLE")
	public ConfigValue<String> TECH_LAST_ATTACK_MAIL_TITLE;
	
	@Static("TECHNOLOGYCOPY:TECH_LAST_ATTACK_MAIL_CONTENT")
	public ConfigValue<String> TECH_LAST_ATTACK_MAIL_CONTENT;
	
	@Static("TECHNOLOGYCOPY:MONSTER_REFRESH_INTERVAL")
	public ConfigValue<Integer> MONSTER_REFRESH_INTERVAL;
	
	@Static("TECHNOLOGYCOPY:BOSS_USE_SKILL_INTERVAL")
	public ConfigValue<Integer> BOSS_USE_SKILL_INTERVAL;
	
	@Static("TECHNOLOGYCOPY:MONSTER_DELETE_DURATION")
	public ConfigValue<Integer> MONSTER_DELETE_DURATION;
	
	@Static("TECHNOLOGYCOPY:MONSTER_HEADING_MAPPING")
	public ConfigValue<Map<String, Integer>> MONSTER_HEADING_MAPPING;
	
	private CoreConditions techCopyStartCond;
	
	private CoreConditions techCopyEnterCond;
	
	public CoreConditions getTechCopyStartCond() {
		if (techCopyStartCond == null) {
			techCopyStartCond = CoreConditionManager.getInstance().getCoreConditions(1, TECH_COPY_START_COND.getValue()); 
		}
		return techCopyStartCond;
	}
	
	public CoreConditions getTechCopyEnterCond() {
		if (techCopyEnterCond == null) {
			techCopyEnterCond = CoreConditionManager.getInstance().getCoreConditions(1, TECH_BORN_ENTER_COND.getValue()); 
		}
		return techCopyEnterCond;
	}
	
}
