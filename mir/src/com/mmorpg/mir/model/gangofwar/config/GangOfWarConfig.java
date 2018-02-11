package com.mmorpg.mir.model.gangofwar.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.anno.Static;

@Component
public class GangOfWarConfig {
	private static GangOfWarConfig self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static GangOfWarConfig getInstance() {
		return self;
	}

	/** 家族战战场 */
	@Static("GOW:MAPID")
	public ConfigValue<Integer> MAPID;

	/** 封印将军重生ID */
	@Static("GOW:SEAL_BOSS_SPAW")
	public ConfigValue<String> SEAL_BOSS_SPAW;

	/** 城门守卫重生ID */
	@Static("GOW:FIRST_GURAD_BOSS_SPAW")
	public ConfigValue<String> FIRST_GURAD_BOSS_SPAW;
	@Static("GOW:SECOND_GURAD_BOSS_SPAW")
	public ConfigValue<String> SECOND_GURAD_BOSS_SPAW;
	@Static("GOW:THIRD_GURAD_BOSS_SPAW")
	public ConfigValue<String> THIRD_GURAD_BOSS_SPAW;

	/** 城门重生ID */
	@Static("GOW:FIRST_DOOR_SPAW")
	public ConfigValue<String> FIRST_DOOR_SPAW;
	@Static("GOW:SECOND_DOOR_SPAW")
	public ConfigValue<String> SECOND_DOOR_SPAW;
	@Static("GOW:THIRD_DOOR_SPAW")
	public ConfigValue<String> THIRD_DOOR_SPAW;

	/** 城门重生statusNpc的ID */
	@Static("GOW:FIRST_DOOR_RELIVE_SPAW")
	public ConfigValue<String> FIRST_DOOR_RELIVE_SPAW;
	@Static("GOW:SECOND_DOOR_RELIVE_SPAW")
	public ConfigValue<String> SECOND_DOOR_RELIVE_SPAW;
	@Static("GOW:THIRD_DOOR_RELIVE_SPAW")
	public ConfigValue<String> THIRD_DOOR_RELIVE_SPAW;

	/** 合服几天内不开家族战 */
	@Static("GOW:MERGE_DATE_NOT_OPEN")
	public ConfigValue<Integer> MERGE_DATE_NOT_OPEN;

	/** 结束时间 */
	@Static("GOW:END_TIME")
	public ConfigValue<Integer> END_TIME;

	/** 转换到防守阶段的延迟时间 */
	@Static("GOW:DELAY_CHANGE_DEFEND_TIME")
	public ConfigValue<Integer> DELAY_CHANGE_DEFEND_TIME;

	/** 进入场景出生坐标 配置格式[22,22] */
	@Static("GOW:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;

	/** 防守时间 */
	@Static("GOW:DEFEND_TIME")
	public ConfigValue<Integer> DEFEND_TIME;

	/** 排行榜最大显示长度 */
	@Static("GOW:KILL_RANK_SIZE")
	public ConfigValue<Integer> KILL_RANK_SIZE;

	/** 家族击杀封印boss时伤害排行榜显示数量 */
	@Static("GOW:GANG_BOSS_DAMAGE_SHOW_MAXSIZE")
	public ConfigValue<Integer> GANG_BOSS_DAMAGE_SHOW_MAXSIZE;

	/** 结算奖励ChooserGroup */
	@Static("GOW:END_REWARD_CHOOSER")
	public ConfigValue<String> END_REWARD_CHOOSER;

	/** 结算时邮件title的I18n */
	@Static("GOW:END_MAIL_TITLE")
	public ConfigValue<String> END_MAIL_TITLE;

	/** 结算时邮件正文的I18n */
	@Static("GOW:END_MAIL_CONTENT")
	public ConfigValue<String> END_MAIL_CONTENT;

	/** 开始的时间 */
	@Static("GOW:START_TIME")
	public ConfigValue<String> START_TIME;

	/** 家族战广播开始的时间 */
	@Static("GOW:NOTICE_START_TIME")
	public ConfigValue<String> NOTICE_START_TIME;

	/** 当没有国王时开始的时间 */
	@Static("GOW:START_TIME_NOT_KING")
	public ConfigValue<String> START_TIME_NOT_KING;

	/** 低级经验 */
	@Static("GOW:EXPHALO_LOW")
	public ConfigValue<Integer> EXPHALO_LOW;
	/** 中级经验 */
	@Static("GOW:EXPHALO_MIDDLE")
	public ConfigValue<Integer> EXPHALO_MIDDLE;
	/** 高级经验 */
	@Static("GOW:EXPHALO_HIGH")
	public ConfigValue<Integer> EXPHALO_HIGH;

	/** 增加经验的statusNpc */
	@Static("GOW:EXP_SPAWS")
	public ConfigValue<String[]> EXP_SPAWS;

	/** 连斩需要通报的次数 格式[10,20,30] */
	@Static("GOW:CONTINUEKILL_TV")
	public ConfigValue<Integer[]> CONTINUEKILL_TV;

	/** 复活点的无敌NPC */
	@Static("GOW:ATTACK_GOD_RELIVE")
	public ConfigValue<String> ATTACK_GOD_RELIVE;
	@Static("GOW:DEFEND_GOD_RELIVE")
	public ConfigValue<String> DEFEND_GOD_RELIVE;

	/** 回城CD时间 毫秒 */
	@Static("GOW:BACKHOME_CD")
	public ConfigValue<Integer> BACKHOME_CD;

	/** （开服活动）王城争霸国君奖励 */
	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_KING_REWARD")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_KING_REWARD;

	/** （开服活动）王城争霸普通玩家奖励 */
	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_COMMON_REWARD")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_COMMON_REWARD;

	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_TITLE")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_TITLE;

	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_CONTENT")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_REWARD_COMMON_MAIL_CONTENT;

	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_TITLE")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_TITLE;

	@Static("GOW:GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_CONTENT")
	public ConfigValue<String> GANG_WAR_OPEN_ACTIVITY_REWARD_KING_MAIL_CONTENT;

}
