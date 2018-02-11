package com.mmorpg.mir.model.kingofwar.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Static;

@Component
public class KingOfWarConfig {
	private static KingOfWarConfig self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static KingOfWarConfig getInstance() {
		return self;
	}

	/** 咸阳战场 */
	@Static("KOW:MAPID")
	public ConfigValue<Integer> MAPID;

	/** 普通咸阳 */
	@Static("PUBLIC:XIANYANG_MAPID")
	public ConfigValue<Integer> XIANYANG_MAPID;

	/** 将军、近卫军重生ID */
	@Static("KOW:BIG_BOSS_SPAW")
	public ConfigValue<String> BIG_BOSS_SPAW;
	@Static("KOW:CENTER_BOSS_SPAW")
	public ConfigValue<String> CENTER_BOSS_SPAW;
	@Static("KOW:LEFT_BOSS_SPAW")
	public ConfigValue<String> LEFT_BOSS_SPAW;
	@Static("KOW:RIGHT_BOSS_SPAW")
	public ConfigValue<String> RIGHT_BOSS_SPAW;

	@Static("KOW:KOW_DRAGON_ID")
	public ConfigValue<Integer> KOW_DRAGON_ID;

	/** 结束时间 */
	@Static("KOW:END_TIME")
	public ConfigValue<Integer> END_TIME;

	/** 3个复活点的状态NPC */
	@Static("KOW:CENTER_STATUSNPC_SPAW")
	public ConfigValue<String> CENTER_STATUSNPC_SPAW;
	@Static("KOW:LEFT_STATUSNPC_SPAW")
	public ConfigValue<String> LEFT_STATUSNPC_SPAW;
	@Static("KOW:RIGHT_STATUSNPC_SPAW")
	public ConfigValue<String> RIGHT_STATUSNPC_SPAW;

	/** 3个复活点的守卫 */
	@Static("KOW:CENTER_COUNTRYNPC_SPAW")
	public ConfigValue<String[]> CENTER_COUNTRYNPC_SPAW;
	@Static("KOW:LEFT_COUNTRYNPC_SPAW")
	public ConfigValue<String[]> LEFT_COUNTRYNPC_SPAW;
	@Static("KOW:RIGHT_COUNTRYNPC_SPAW")
	public ConfigValue<String[]> RIGHT_COUNTRYNPC_SPAW;

	/** 进入场景出生坐标 配置格式[22,22] */
	@Static("KOW:BORN_POSITION")
	public ConfigValue<Integer[]> BORN_POSITION;

	// /** 皇帝诞生时的广播i18n id */
	// @Static("KOW:KING_RETURN_MESSAGE")
	// public ConfigValue<String> KING_RETURN_MESSAGE;

	/** 开场保护的时间,毫秒 */
	@Static("KOW:START_PROTECT_TIME")
	public ConfigValue<Integer> START_PROTECT_TIME;

	/** 杀敌一个获得积分 */
	@Static("KOW:KILLOR_PLAYER_POINTS")
	public ConfigValue<Integer> KILLOR_PLAYER_POINTS;

	/** 助攻的积分 */
	@Static("KOW:ASSIST_PLAYER_POINTS")
	public ConfigValue<Integer> ASSIST_PLAYER_POINTS;

	/** 助攻过期时间 毫秒 */
	@Static("KOW:ASSIST_PLAYER_TIME")
	public ConfigValue<Integer> ASSIST_PLAYER_TIME;

	/** 被击杀获得积分 */
	@Static("KOW:KILLED_PLAYER_POINTS")
	public ConfigValue<Integer> KILLED_PLAYER_POINTS;

	/** 杀同一个玩家多少次没有积分 */
	@Static("KOW:KILLED_PLAYER_NOPOINTS_COUNT")
	public ConfigValue<Integer> KILLED_PLAYER_NOPOINTS_COUNT;

	/** 复活点占领以后施加的BUFF技能ID */
	@Static("KOW:RELIVE_BUFF_SKILL")
	public ConfigValue<Integer> RELIVE_BUFF_SKILL;

	/** 击杀禁卫军积分奖励 */
	@Static("KOW:KILL_GUARD_POINTS")
	public ConfigValue<Integer> KILL_GUARD_POINTS;

	/** 击杀禁卫军同国成员积分奖励 */
	@Static("KOW:KILL_GUARD_COUNTRY_POINTS")
	public ConfigValue<Integer> KILL_GUARD_COUNTRY_POINTS;

	/** 击杀大将军积分奖励 */
	@Static("KOW:KILL_BIGBOSS_POINTS")
	public ConfigValue<Integer> KILL_BIGBOSS_POINTS;

	/** 积分排行榜最大显示长度 */
	@Static("KOW:POINTS_RANK_SIZE")
	public ConfigValue<Integer> POINTS_RANK_SIZE;

	/** BOSS战斗自动加积分时间 配置格式[0,300,600] */
	@Static("KOW:BOSS_POINTS_TIME")
	public ConfigValue<Integer[]> BOSS_HALOPOINTS_TIME;

	/** 大将军同屏战斗加积分， 配置格式[6,4,2] */
	@Static("KOW:BIGBOSS_HALOPOINTS_VALUE")
	public ConfigValue<Integer[]> BIGBOSS_HALOPOINTS_VALUE;

	/** 禁卫军同屏战斗加积分， 配置格式[3,2,1] */
	@Static("KOW:GUARDBOSS_HALOPOINTS_VALUE")
	public ConfigValue<Integer[]> GUARDBOSS_HALOPOINTS_VALUE;

	/** BOSS同屏加积分周期 毫秒 */
	@Static("KOW:HALOPOINTS_PERIOD")
	public ConfigValue<Integer> HALOPOINTS_PERIOD;

	/** 邮件发送NPC筛选器,id */
	@Static("KOW:MAIL_SENDER_NPC_CHOOSER")
	public ConfigValue<String> MAIL_SENDER_NPC_CHOOSER;

	/** 开启时间 */
	@Static("KOW:START_TIME")
	public ConfigValue<String> START_TIME;

	/** 开启时间,周 */
	@Static("KOW:START_TIME_WEEK")
	public ConfigValue<String> START_TIME_WEEK;

	/** 开启时间 前的公告 */
	@Static("KOW:START_TIME_NOTICE")
	public ConfigValue<String> START_TIME_NOTICE;

	/** 合服几天内不开皇城战 */
	@Static("KOW:MERGE_DATE_NOT_OPEN")
	public ConfigValue<Integer> MERGE_DATE_NOT_OPEN;

	/** 复活点积分 */
	@Static("KOW:RELIVE_POINTS")
	public ConfigValue<Integer> RELIVE_POINTS;

	/** 复活点积分增加时间周期毫秒 */
	@Static("KOW:RELIVE_POINTS_PERIOD")
	public ConfigValue<Integer> RELIVE_POINTS_PERIOD;

	/** 结算奖励ChooserGroup */
	@Static("KOW:END_REWARD_CHOOSER")
	public ConfigValue<String> END_REWARD_CHOOSER;

	/** 结算时邮件title的I18n */
	@Static("KOW:END_MAIL_TITLE")
	public ConfigValue<String> END_MAIL_TITLE;

	/** 结算时邮件正文的I18n */
	@Static("KOW:END_MAIL_CONTENT")
	public ConfigValue<String> END_MAIL_CONTENT;

	/** 军衔发放将的等级>= */
	@Static("KOW:MILITART_REWARD_LEVEL")
	public ConfigValue<Integer> MILITART_REWARD_LEVEL;

	/** 军饷发奖的值 百分比0-100 */
	@Static("KOW:MILITART_REWARD_VALUE")
	public ConfigValue<Integer> MILITART_REWARD_VALUE;

	/** 皇帝每日奖励 */
	@Static("KOW:KINGOFKING_DAY_REWARD")
	public ConfigValue<String> KINGOFKING_DAY_REWARD;

	/** 连斩需要通报的次数,（阻止）格式[10,20,30] */
	@Static("KOW:CONTINUEKILL_TV_JUNIOR")
	public ConfigValue<Integer[]> CONTINUEKILL_TV_JUNIOR;

	/** 连斩需要通报的次数（超神） 格式[50,60...] */
	@Static("KOW:CONTINUEKILL_TV_SENIOR")
	public ConfigValue<Integer[]> CONTINUEKILL_TV_SENIOR;

	/** 被击杀一次炸弹值增加量 */
	@Static("KOW:BOMB_VALUE")
	public ConfigValue<Integer> BOMB_VALUE;

	/** 最大炸弹值（可爆时的怒气） */
	@Static("KOW:BOMB_MAX_VALUE")
	public ConfigValue<Integer> BOMB_MAX_VALUE;

	/** 炸弹人标记BUFF技能ID） */
	@Static("KOW:BOMB_BUFF_SKILL")
	public ConfigValue<Integer> BOMB_BUFF_SKILL;

	/** 使用炸弹人技能ID） */
	@Static("KOW:BOMB_SKILL")
	public ConfigValue<Integer> BOMB_SKILL;

	/** 皇帝增加的属性 */
	@Static("KOW:KINGOFKING_STATS")
	public ConfigValue<Stat[]> KINGOFKING_STATS;

	/** 皇城战参加条件 */
	@Static("KOW:ENTER_CONDITIONS")
	public ConfigValue<String[]> ENTER_CONDITIONS;

	/** 皇帝每日礼包邮件标题il18n */
	@Static("KOW:KING_DAILY_REWARD_MAIL_TITLE")
	public ConfigValue<String> KING_DAILY_REWARD_MAIL_TITLE;

	/** 皇帝每日礼包邮件内容il18n */
	@Static("KOW:KING_DAILY_REWARD_MAIL_CONTENT")
	public ConfigValue<String> KING_DAILY_REWARD_MAIL_CONTENT;

	/** 皇城战前100名最低奖励积分 */
	@Static("KOW:KING_WAR_END_REWARD_LEAST_POINT")
	public ConfigValue<Integer> KING_OF_WAR_END_REWARD_LEAST_POINT;

	/** 皇城战参与奖 */
	@Static("KOW:KING_OF_WAR_ATTEND_REWARD_ID")
	public ConfigValue<String> KING_OF_WAR_ATTEND_REWARD_ID;

	/** 皇城战参与奖最低积分要求 */
	@Static("KOW:KING_OF_WAR_ATTEND_REWARD_LEAST_POINT")
	public ConfigValue<Integer> KING_OF_WAR_ATTEND_REWARD_LEAST_POINT;

	/** 皇城争霸皇帝额外奖励邮件 标题il18n */
	@Static("KOW:KING_ADDITION_REWARD_MAIL_IL18N")
	public ConfigValue<String> KING_ADDITION_REWARD_TITLE_IL18N;

	/** 皇城争霸皇帝额外奖励邮件正文Il18n */
	@Static("KOW:KING_ADDITION_REWARD_CONTENT_IL18N")
	public ConfigValue<String> KING_ADDITION_REWARD_CONTENT_IL18N;

	/** 皇城争霸皇帝所在国额外奖励邮件标题il18n */
	@Static("KOW:WIN_ADDITION_REWARD_TITLE_IL18N")
	public ConfigValue<String> WIN_ADDITION_REWARD_TITLE_IL18N;

	/** 皇城争霸皇帝所在国额外奖励正文il18n */
	@Static("KOW:WIN_ADDITION_REWARD_CONTENT_IL18N")
	public ConfigValue<String> WIN_ADDITION_REWARD_CONTENT_IL18N;

	/** 皇城争霸其他国家额外奖励标题il18n */
	@Static("KOW:OTHER_ADDITION_REWARD_TITLE_IL18N")
	public ConfigValue<String> OTHER_ADDITION_REWARD_TITLE_IL18N;

	/** 皇城争霸其他国家额外奖励邮件正文il18n */
	@Static("KOW:OTHER_ADDITION_REWARD_CONTENT_IL18N")
	public ConfigValue<String> OTHER_ADDITION_REWARD_CONTENT_IL18N;

	/** 皇城争霸皇帝额外奖励 */
	@Static("KOW:KING_ADDITION_REWARD")
	public ConfigValue<String> KING_ADDITION_REWARD;

	/** 皇帝所在国额外奖励 */
	@Static("KOW:WIN_ADDITION_REWARD")
	public ConfigValue<String> WIN_ADDITION_REWARD;

	/** 皇城争霸其他国奖励 */
	@Static("KOW:OTHER_ADDITION_REWARD")
	public ConfigValue<String> OTHER_ADDITION_REWARD;

	@Static("KOW:KINGOFKING_CAN_FORBID")
	public ConfigValue<Boolean> KINGOFKING_CAN_FORBID;
}
