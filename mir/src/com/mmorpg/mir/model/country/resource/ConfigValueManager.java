package com.mmorpg.mir.model.country.resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class ConfigValueManager {

	private static ConfigValueManager self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static ConfigValueManager getInstance() {
		return self;
	}

	@Static
	public Storage<Integer, CountryBuildValueFixResource> countryBuildValueStorage;

	@Static
	public Storage<Integer, CountryTechnologyResource> countryTechnologyStorage;

	/** 国家召集国民 在皇城争夺战期间,可被召集次数+2 */
	@Static("COUNTRY:CALL_TOGETHER_ADD_NUM")
	public ConfigValue<Integer> CALL_TOGETHER_ADD_NUM;

	/** 国家对应的大臣NPC(特殊怪） 的位置 */
	@Static("COUNTRY:COUNTRY_DIP_POS")
	public ConfigValue<Map<String, Map<String, String>>> COUNTRY_DIP_POS;

	/** 国家对应的国旗NPC(特殊怪） 的位置 */
	@Static("COUNTRY:COUNTRY_FLAG_POS")
	public ConfigValue<Map<String, Map<String, String>>> COUNTRY_FLAG_POS;

	/** 国家日志保留的最多条数 */
	@Static("COUNTRY:STORAGE_LOG_MAX_COUNT")
	public ConfigValue<String[]> STORAGE_LOG_MAX_COUNT;

	@Static("COUNTRY:COUNTRY_TEMPLE_QUEST_MAX_BRICK")
	public ConfigValue<Integer> COUNTRY_TEMPLE_QUEST_MAX_BRICK;

	@Static("COUNTRY:COUNTRY_TEMPLE_ACCPET_BRICK_QUEST_CHOOSER")
	public ConfigValue<String> COUNTRY_TEMPLE_ACCPET_BRICK_QUEST_CHOOSER;

	@Static("COUNTRY:COUNTRY_TEMPLE_CHANGE_BRICK_CD")
	public ConfigValue<Integer> COUNTRY_TEMPLE_CHANGE_BRICK_CD;

	@Static("COUNTRY:COUNTRY_TEMPLE_INIT_BRICK")
	public ConfigValue<Integer> COUNTRY_TEMPLE_INIT_BRICK;

	@Static("INVESTIGATE:INVESTIGATE_MAX_COUNT")
	public ConfigValue<Integer> INVESTIGATE_MAX_COUNT;

	@Static("INVESTIGATE:INVESTIGATE_SELECT_NPC")
	public ConfigValue<String> INVESTIGATE_SELECT_NPC;

	@Static("INVESTIGATE:INVESTIGATE_SELECT_CHANGE_CD")
	public ConfigValue<Integer> INVESTIGATE_SELECT_CHANGE_CD;

	@Static("COUNTRY:COUNTRY_TEMPLE_CHANGE_BRICK_CONDITIONS")
	public ConfigValue<Map<Integer, String[]>> COUNTRY_TEMPLE_CHANGE_BRICK_CONDITIONS;

	/** 发放官员福利的公告 */
	@Static("COUNTRY:COUNTRY_OPEN_OFFICAL_SALARY")
	public ConfigValue<String> COUNTRY_OPEN_OFFICAL_SALARY;

	@Static("COUNTRY:COUNTRY_OPEN_NATIONAL_SALARY")
	public ConfigValue<String> COUNTRY_OPEN_NATIONAL_SALARY;

	/** 领取国民福利条件 */
	@Static("COUNTRY:COUNTRY_RECEIVE_SALARY")
	public ConfigValue<String[]> COUNTRY_RECEIVE_SALARY;

	/** 领取官员福利条件 */
	@Static("COUNTRY:COUNTRY_RECEIVE_OFFICIAL_SALARY")
	public ConfigValue<String[]> COUNTRY_RECEIVE_OFFICIAL_SALARY;

	/** 发放国民福利条件 */
	@Static("COUNTRY:COUNTRY_OPEN_CIVIL_SALARY_CONDITION")
	public ConfigValue<String[]> COUNTRY_OPEN_CIVIL_SALARY_CONDITION;

	/** 发放官员福利条件 */
	@Static("COUNTRY:COUNTRY_OPEN_OFFICIAL_SALARY_CONDITION")
	public ConfigValue<String[]> COUNTRY_OPEN_OFFICIAL_SALARY_CONDITION;

	/** 召集国民人数限制 */
	@Static("COUNTRY:CALL_TOGETHER_COUNT_LIMIT")
	public ConfigValue<Integer> CALL_TOGETHER_COUNT_LIMIT;

	@Static("PUBLIC:BIRTH_POINT")
	public ConfigValue<String> BIRTH_POINT;

	@Static("COUNTRY:COUNTRY_CONTROL_INIT")
	public ConfigValue<Integer> COUNTRY_CONTROL_INIT;

	@Static("COUNTRY:COUNTRY_CONTROL_MAX")
	public ConfigValue<Integer> COUNTRY_CONTROL_MAX;

	@Static("COUNTRY:COUNTRY_CONTROL_DAY_REDUCE")
	public ConfigValue<Integer> COUNTRY_CONTROL_DAY_REDUCE;

	@Static("COUNTRY:COUNTRY_CONTRIBUTION")
	public ConfigValue<Map<CoppersType, String>> COUNTRY_CONTRIBUTION;

	@Static("COUNTRY:COUNTRY_CONTRIBUTION_REWARD")
	public ConfigValue<Map<CoppersType, String>> COUNTRY_CONTRIBUTION_REWARD;

	@Static("COUNTRY:COUNTRY_SHOP_REFRESH_SIZE")
	public ConfigValue<Integer> COUNTRY_SHOP_REFRESH_SIZE;

	@Static("COUNTRY:COUNTRY_CONTRIBUTION_REWARD1")
	public ConfigValue<Map<CoppersType, String>> COUNTRY_CONTRIBUTION_REWARD1;

	@Static("COUNTRY:COUNTRY_SHOP_UPGRADE_ACTION")
	public ConfigValue<Map<String, String>> COUNTRY_SHOP_UPGRADE_ACTION;

	@Static("COUNTRY:COUNTRY_NAMES")
	public ConfigValue<Map<String, String>> COUNTRY_NAMES;

	@Static("COUNTRY:COUNTRY_SHOP_REFRESHTIME")
	public ConfigValue<Integer> COUNTRY_SHOP_REFRESHTIME;

	@Static("COUNTRY:COUNTRY_APPOINT_GUARD")
	public ConfigValue<String[]> COUNTRY_APPOINT_GUARD;

	@Static("COUNTRY:COUNTRY_CALL_TOGETHER")
	public ConfigValue<String[]> COUNTRY_CALL_TOGETHER;

	// /** 卫队召集，过期时间毫秒 */
	// @Static("COUNTRY:COUNTRY_CALL_TOGETHER_DEPRECATED_TIME")
	// public ConfigValue<Integer> COUNTRY_CALL_TOGETHER_DEPRECATED_TIME;

	@Static("COUNTRY:COUNTRY_CALL_TOGETHER_CD")
	public ConfigValue<Integer> COUNTRY_CALL_TOGETHER_CD;

	@Static("COUNTRY:COUNTRY_FORBIDCHAT_TIME")
	public ConfigValue<Integer> COUNTRY_FORBIDCHAT_TIME;

	@Static("COUNTRY:COUNTRY_TRAITOR_SKILLID")
	public ConfigValue<Integer> COUNTRY_TRAITOR_SKILLID;

	@Static("COUNTRY:COUNTRY_OFFICAL_SALARY_CHOOSERGROUP")
	public ConfigValue<String> COUNTRY_OFFICAL_SALARY_CHOOSERGROUP;

	@Static("COUNTRY:COUNTRY_CIVIL_SALARY_CHOOSERGROUP")
	public ConfigValue<String> COUNTRY_CIVIL_SALARY_CHOOSERGROUP;

	@Static("COUNTRY:COUNTRY_CIVIL_SALARY_RESETTIME")
	public ConfigValue<String> COUNTRY_CIVIL_SALARY_RESETTIME;

	public String getCivilSalryResettime() {
		return COUNTRY_CIVIL_SALARY_RESETTIME.getValue();
	}

	@Static("COUNTRY:COUNTRY_OFFICIAL_SALARY_RESETTIME")
	public ConfigValue<String> COUNTRY_OFFICIAL_SALARY_RESETTIME;

	@Static("COUNTRY:COUNTRY_USECALLTOKEN_CONDITIONS")
	public ConfigValue<String[]> COUNTRY_USECALLTOKEN_CONDITIONS;

	@Static("COUNTRY:COUNTRY_STORE_MAX_SIZE")
	public ConfigValue<Integer> COUNTRY_STORE_MAX_SIZE;

	@Static("COUNTRY:COUNTRY_STORE_INIT_SIZE")
	public ConfigValue<Integer> COUNTRY_STORE_INIT_SIZE;

	@Static("COUNTRY:COUNTRY_TEMPLE_LOCAL")
	public ConfigValue<Map<Integer, String>> COUNTRY_TEMPLE_LOCAL;

	@Static("COUNTRY:COUNTRY_TEMPLE_BRICK_CONDITIONS")
	public ConfigValue<Map<String, ArrayList<String>>> COUNTRY_TEMPLE_BRICK_CONDITIONS;

	@Static("COUNTRY:COUNTRY_TEMPLE_BRICK_DEADTIME")
	public ConfigValue<Integer> COUNTRY_TEMPLE_BRICK_DEADTIME;

	@Static("COUNTRY:COUNTRY_TEMPLE_PUT_BRICK_CONDITIONS")
	public ConfigValue<Map<Integer, String[]>> COUNTRY_TEMPLE_PUT_BRICK_CONDITIONS;

	@Static("COUNTRY:COUNTRY_TEMPLE_MAX_BRICK")
	public ConfigValue<Integer> COUNTRY_TEMPLE_MAX_BRICK;

	/** 国家禁言次数不足 错误码code */
	@Static("COUNTRY:COUNTRY_FIRBIDCHAT_ERRORCODE")
	public ConfigValue<String> COUNTRY_FIRBIDCHAT_ERRORCODE;

	/** 国家对应的大臣NPC ID */
	@Static("COUNTRY:COUNTRY_DIP_NPC")
	public ConfigValue<Map<String, String>> COUNTRY_DIP_NPC;

	/** 国家对应的国旗NPC ID */
	@Static("COUNTRY:COUNTRY_DIP_NPC_FLAG")
	public ConfigValue<Map<String, String>> COUNTRY_DIP_NPC_FLAG;

	/** 虎符拥有人数限制 */
	@Static("COUNTRY:COUNTRY_TOGETHERTOKENS_SIZE")
	public ConfigValue<String> COUNTRY_TOGETHERTOKENS_SIZE;

	/** 大臣被打死后统治力下降点数 */
	@Static("COUNTRY:COUNTRY_DIP_NPC_DIE")
	public ConfigValue<Integer> COUNTRY_DIP_NPC_DIE;

	/** 大臣NPC脱战时间 - 秒 */
	@Static("COUNTRY:COUNTRY_DIP_LEAVE_ATTACKED_TIME")
	public ConfigValue<Integer> COUNTRY_DIP_LEAVE_ATTACKED_TIME;

	/** 国旗NPC脱战时间 - 秒 */
	@Static("COUNTRY:COUNTRY_FLAG_LEAVE_ATTACKED_TIME")
	public ConfigValue<Integer> COUNTRY_FLAG_LEAVE_ATTACKED_TIME;

	/** 大臣被攻击广播求救频率-秒 */
	@Static("COUNTRY:COUNTRY_NPC_BROADCAST_SECOND")
	public ConfigValue<Integer> COUNTRY_NPC_BROADCAST_SECOND;

	/** 国旗被攻击广播求救频率-秒 */
	@Static("COUNTRY:COUNTRY_NPC_BROADCAST_SECOND_FLAG")
	public ConfigValue<Integer> COUNTRY_NPC_BROADCAST_SECOND_FLAG;

	/** 大臣血量剩余提醒 */
	@Static("COUNTRY:COUNTRY_NPC_HP_TIPS")
	public ConfigValue<Map<String, String>> COUNTRY_NPC_HP_TIPS;

	/** 攻击国的国旗血量剩余提醒 */
	@Static("COUNTRY:HITER_DIPLOMACY_HP_TIPS")
	public ConfigValue<Integer[]> HITER_DIPLOMACY_HP_TIPS;

	/** 国旗血量剩余提醒 */
	@Static("COUNTRY:COUNTRY_NPC_HP_TIPS_FLAG")
	public ConfigValue<Map<String, String>> COUNTRY_NPC_HP_TIPS_FLAG;

	/** 攻击国的国旗血量剩余提醒 */
	@Static("COUNTRY:HITER_FLAG_HP_TIPS")
	public ConfigValue<Integer[]> HITER_FLAG_HP_TIPS;

	/** 大臣回血的时间间隔 每隔这么多时间回血一次 -秒 */
	@Static("COUNTRY:COUNTRY_NPC_BACK_HP_TIME")
	public ConfigValue<Integer> COUNTRY_NPC_BACK_HP_TIME;

	/** 大臣回血的点数 */
	@Static("COUNTRY:COUNTRY_NPC_BACK_HP")
	public ConfigValue<Integer> COUNTRY_NPC_BACK_HP;

	/** 国家大臣NPC安全期任务任务开始表达式 */
	@Static("COUNTRY:COUNTRY_SAFE_START_CRON")
	public ConfigValue<String> COUNTRY_SAFE_START_CRON;

	@Static("COUNTRY:COUNTRY_SAFE_START_CRON_FLAG")
	public ConfigValue<String> COUNTRY_SAFE_START_CRON_FLAG;

	/** 国家大臣NPC安全期任务任务结束表达式 */
	@Static("COUNTRY:COUNTRY_SAFE_END_CRON")
	public ConfigValue<String> COUNTRY_SAFE_END_CRON;

	@Static("COUNTRY:COUNTRY_SAFE_END_CRON_FLAG")
	public ConfigValue<String> COUNTRY_SAFE_END_CRON_FLAG;

	@Static("COUNTRY:KING_CITY_MAPID")
	public ConfigValue<String[]> KING_CITY_MAPID;

	@Static("COUNTRY:OFFICIAL_NAME")
	public ConfigValue<Map<String, String>> OFFICIAL_NAME;

	/** 国旗倒下，经验减半时间 秒 */
	@Static("COUNTRY:DIED_BUFF_FLAG_EXPTIME")
	public ConfigValue<Integer> DIED_BUFF_FLAG_EXPTIME;

	/** 国王发布 国家搬砖后持续的时间 秒 */
	@Static("COUNTRY:QUEST_TEMPLE_TIME")
	public ConfigValue<Integer> QUEST_TEMPLE_TIME;
	/** 国王发布 国家运镖后持续的时间 秒 */
	@Static("COUNTRY:QUEST_EXPRESS_TIME")
	public ConfigValue<Integer> QUEST_EXPRESS_TIME;

	/** 国王发布国家搬砖的条件（比如发布的时间限制） */
	@Static("COUNTRY:QUEST_TEMPLE_TIME_CONDITIONS")
	public ConfigValue<String[]> QUEST_TEMPLE_TIME_CONDITIONS;
	/** 国王发布国家运镖的条件（比如发布的时间限制） */
	@Static("COUNTRY:QUEST_EXPRESS_TIME_CONDITIONS")
	public ConfigValue<String[]> QUEST_EXPRESS_TIME_CONDITIONS;

	/** 国家搬砖 自动开启时间 （CRON表达式 0 0 12 * * *） */
	@Static("COUNTRY:QUEST_TEMPLE_AUTO_STARTTIME")
	public ConfigValue<String> QUEST_TEMPLE_AUTO_STARTTIME;

	/** 国家运镖 自动开启时间 （CRON表达式 0 0 12 * * *） */
	@Static("COUNTRY:QUEST_EXPRESS_AUTO_STARTTIME")
	public ConfigValue<String> QUEST_EXPRESS_AUTO_STARTTIME;

	@Static("COUNTRY:CIVIL_NPC_LOACTION")
	public ConfigValue<Map<String, Map<String, String>>> CIVIL_NPC_LOACTION;

	@Static("COUNTRY:OFFICIAL_NPC_LOACTION")
	public ConfigValue<Map<String, Map<String, String>>> OFFICIAL_NPC_LOACTION;

	@Static("COUTNRY:FREE_RELIVE_SKILL_ID")
	public ConfigValue<Integer> FREE_RELIVE_SKILL_ID;

	@Static("COUTNRY:FLAG_DEAD_DEBUFF_VALUE")
	public ConfigValue<Double> FLAG_DEAD_DEBUFF_VALUE;

	@Static("COUNTRY:FETE_COPPER_ACTION")
	public ConfigValue<String> FETE_COPPER_ACTION;

	@Static("COUNTRY:FETE_COINS_ACTION")
	public ConfigValue<String> FETE_COINS_ACTION;

	@Static("COUNTRY:FETE_COPPER_REWARD")
	public ConfigValue<String> FETE_COPPER_REWARD;

	@Static("COUNTRY:FETE_COINS_REWARD")
	public ConfigValue<String> FETE_COINS_REWARD;

	@Static("COUNTRY:COUNTRY_ACT_DISPLAY_MAPID")
	public ConfigValue<Integer[]> COUNTRY_ACT_DISPLAY_MAPID;

	@Static("COUNTRY:OFFICIAL_MOBILIZATION_ACCEPT_CONDITIONID")
	public ConfigValue<String[]> OFFICIAL_MOBILIZATION_ACCEPT_CONDITIONID;

	@Static("COUNTRY:OFFICIAL_MOBILIZATION_ACTS")
	public ConfigValue<String[]> OFFICIAL_MOBILIZATION_ACTS;

	@Static("COUNTRY:OFFICIAL_MOBILIZATION_NPC")
	public ConfigValue<Map<String, Map<String, String>>> OFFICIAL_MOBILIZATION_NPC;

	@Static("COUNTRY:HIDDEN_DAILY_MISSION")
	public ConfigValue<Map<String, Integer>> HIDDEN_DAILY_MISSION;

	@Static("COUNTRY:HIDDEN_MISSION_PER_COUNT")
	public ConfigValue<Map<String, Integer>> HIDDEN_MISSION_PER_COUNT;

	@Static("COUNTRY:HIDDEN_MISSION_REWARDID")
	public ConfigValue<Map<String, String>> HIDDEN_MISSION_REWARDID;

	public String getCountryFlagRewardId(CountryFlagQuestType type, byte lost) {
		if (lost == 1) {
			if (type == CountryFlagQuestType.DEFENCE) {
				return FLAG_DEFEND_FAIL_REWARDID.getValue();
			} else {
				return FLAG_ATTACK_FAIL_REWARDID.getValue();
			}
		} else {
			return HIDDEN_MISSION_REWARDID.getValue().get(HiddenMissionType.DEFEND_FLAG.name());
		}
	}

	@Static("COUNTRY:AURA_STATSKILLID")
	public ConfigValue<Integer> AURA_STATSKILLID;

	@Static("COUNTRY:AURA_APPLYTO_STATSKILLID")
	public ConfigValue<Integer> AURA_APPLYTO_STATSKILLID;

	/** 国王上线通报CD 秒 */
	@Static("COUNTRY:KING_LOGIN_BROAD_CD")
	public ConfigValue<Integer> KING_LOGIN_BROAD_CD;

	/** 开服创建角色人数 */
	@Static("PUBLIC:OPENSERVER_CREATEPLAYER_COUNT")
	public ConfigValue<Integer> OPENSERVER_CREATEPLAYER_COUNT;

	@Static("COUNTRY:SACRIFICE_TOTAL_SIZE")
	public ConfigValue<Integer> SACRIFICE_TOTAL_SIZE;

	@Static("COUNTRY:SACRIFICE_LASTED_SIZE")
	public ConfigValue<Integer> SACRIFICE_LASTED_SIZE;

	@Static("COUNTRY:OFFICIAL_MOBILIZATION_DAILY_COUNT")
	public ConfigValue<Map<String, Integer>> OFFICIAL_MOBILIZATION_DAILY_COUNT;

	@Static("COUNTRY:OFFICIAL_FORBID_DAILY_COUNT")
	public ConfigValue<Map<String, Integer>> OFFICIAL_FORBID_DAILY_COUNT;

	@Static("COUNTRY:EXPRESS_FLY_DESINATION")
	public ConfigValue<RelivePosition[]> EXPRESS_FLY_DESINATION;

	@Static("PUBLIC:MONSTER_DIE_DP")
	public ConfigValue<Integer> MONSTER_DIE_DP;

	@Static("COUNTRY:SCULPTURE_SPAWN_ID")
	public ConfigValue<Map<String, ArrayList<String>>> SCULPTURE_SPAWN_ID;

	@Static("COUNTRY:SCULPTURE_CARVE_CONDITION")
	public ConfigValue<Map<String, ArrayList<String>>> SCULPTURE_CARVE_CONDITION;

	@Static("COUNTRY:FLAG_BUFF_LEVEL_MIN")
	public ConfigValue<Integer> COUNTRY_FLAG_BUFF_LEVEL_MIN;

	@Static("PUBLIC:PLAYER_INIT_DP")
	public ConfigValue<Integer> PLAYER_INIT_DP;

	/** 国旗死亡墓碑 */
	@Static("COUNTRY:FLAG_BOMBSTONE_SPAWNIDS")
	public ConfigValue<Map<String, String>> COUNTRY_FLAG_BOMBSTONE_SPAWNIDS;

	@Static("COUNTRY:DIPLOMACY_BOMBSTONE_SPAWNID")
	public ConfigValue<Map<String, String>> COUNTRY_DIPLOMACY_BOMBSTONE_SPAWNIDS;

	@Static("COUNTRY:KILL_DIPLOMACY_FILTER_CONDS")
	public ConfigValue<String[]> KILL_DIPLOMACY_FILTER_CONDS;

	@Static("COUNTRY:KILL_COUNTRYFLAG_FILTER_CONDS")
	public ConfigValue<String[]> KILL_COUNTRYFLAG_FILTER_CONDS;

	@Static("COUNTRY:RANK_SIZE")
	public ConfigValue<Integer> RANK_SIZE;

	@Static("COUNTRY:COUNTRY_AUTHORITY_EXTRA_COUNT")
	public ConfigValue<Map<String, Integer>> COUNTRY_AUTHORITY_EXTRA_COUNT;

	@Static("COUNTRY:STARTEGY_PAOPAO_CONDS")
	public ConfigValue<Map<String, ArrayList<String>>> STARTEGY_PAOPAO_CONDS;

	@Static("COUNTRY:WEAK_COUNTRY_CALL_COUNT")
	public ConfigValue<Integer> WEAK_COUNTRY_CALL_COUNT;

	@Static("COUNTRY:COUNTRY_CALL_GUARD_COUNT")
	public ConfigValue<Integer> COUNTRY_CALL_GUARD_COUNT;

	@Static("COUNTRY:COUNTRY_LEVEL_FACTOR")
	public ConfigValue<Double[]> COUNTRY_LEVEL_FACTOR;

	@Static("COUNTRY:TRAITOR_MAX_LIMIT")
	public ConfigValue<Integer> TRAITOR_MAX_LIMIT;

	@Static("COUNTRY:CUT_FLAG_ATTEND_REWARD")
	public ConfigValue<String> CUT_FLAG_ATTEND_REWARD;

	@Static("COUNTRY:CUT_FLAG_ATTEND_TITLE")
	public ConfigValue<String> CUT_FLAG_ATTEND_TITLE;

	@Static("COUNTRY:CUT_FLAG_ATTEND_CONTENT")
	public ConfigValue<String> CUT_FLAG_ATTEND_CONTENT;

	@Static("COUNTRY:DEFEND_FLAG_ATTEND_REWARD")
	public ConfigValue<String> DEFEND_FLAG_ATTEND_REWARD;

	@Static("COUNTRY:DEFEND_FLAG_ATTEND_TITLE")
	public ConfigValue<String> DEFEND_FLAG_ATTEND_TITLE;

	@Static("COUNTRY:DEFEND_FLAG_ATTEND_CONTENT")
	public ConfigValue<String> DEFEND_FLAG_ATTEND_CONTENT;

	@Static("COUNTRY:CUT_FLAG_FAIL_ATTEND_REWARD")
	public ConfigValue<String> CUT_FLAG_FAIL_ATTEND_REWARD;

	@Static("COUNTRY:CUT_FLAG_FAIL_ATTEND_TITLE")
	public ConfigValue<String> CUT_FLAG_FAIL_ATTEND_TITLE;

	@Static("COUNTRY:CUT_FLAG_FAIL_ATTEND_CONTENT")
	public ConfigValue<String> CUT_FLAG_FAIL_ATTEND_CONTENT;

	@Static("COUNTRY:DEFEND_FLAG_FAIL_ATTEND_REWARD")
	public ConfigValue<String> DEFEND_FLAG_FAIL_ATTEND_REWARD;

	@Static("COUNTRY:DEFEND_FLAG_FAIL_ATTEND_TITLE")
	public ConfigValue<String> DEFEND_FLAG_FAIL_ATTEND_TITLE;

	@Static("COUNTRY:DEFEND_FLAG_FAIL_ATTEND_CONTENT")
	public ConfigValue<String> DEFEND_FLAG_FAIL_ATTEND_CONTENT;

	public String getFlagRewardFullRewardId(CountryFlagQuestType type, byte lost) {
		if (lost == 1) {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_FAIL_ATTEND_REWARD.getValue();
			} else {
				return CUT_FLAG_FAIL_ATTEND_REWARD.getValue();
			}
		} else {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_ATTEND_REWARD.getValue();
			} else {
				return CUT_FLAG_ATTEND_REWARD.getValue();
			}
		}
	}

	public String getFlagFullRewardMailTitle(CountryFlagQuestType type, byte lost) {
		if (lost == 1) {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_FAIL_ATTEND_TITLE.getValue();
			} else {
				return CUT_FLAG_FAIL_ATTEND_TITLE.getValue();
			}
		} else {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_ATTEND_TITLE.getValue();
			} else {
				return CUT_FLAG_ATTEND_TITLE.getValue();
			}
		}
	}

	public String getFlagFullRewardMailContent(CountryFlagQuestType type, byte lost) {
		if (lost == 1) {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_FAIL_ATTEND_CONTENT.getValue();
			} else {
				return CUT_FLAG_FAIL_ATTEND_CONTENT.getValue();
			}
		} else {
			if (type == CountryFlagQuestType.DEFENCE) {
				return DEFEND_FLAG_ATTEND_CONTENT.getValue();
			} else {
				return CUT_FLAG_ATTEND_CONTENT.getValue();
			}
		}
	}

	@Static("COUNTRY:CUT_DIPLOMACY_TITLE")
	public ConfigValue<String> CUT_DIPLOMACY_TITLE;

	@Static("COUNTRY:CUT_DIPLOMACY_CONTENT")
	public ConfigValue<String> CUT_DIPLOMACY_CONTENT;

	@Static("COUNTRY:CUT_DIPLOMACY_ATTEND_REWARD")
	public ConfigValue<String> CUT_DIPLOMACY_ATTEND_REWARD;

	@Static("COUNTRY:AUTO_APPOINT_OFFICAL_MAIL_TITLEID")
	public ConfigValue<String> AUTO_APPOINT_OFFICAL_MAIL_TITLEID;

	@Static("COUNTRY:AUTO_APPOINT_OFFICAL_MAIL_CONTENTID")
	public ConfigValue<String> AUTO_APPOINT_OFFICAL_MAIL_CONTENTID;

	@Static("COUNTRY:COUNTRYFLAG_QUEST_RECEIVE_CONDS")
	public ConfigValue<String[]> COUNTRYFLAG_QUEST_RECEIVE_CONDS;

	private CoreConditions attendConditions;

	public boolean filterFlagAttend(Player player) {
		if (attendConditions == null) {
			attendConditions = CoreConditionManager.getInstance().getCoreConditions(1,
					COUNTRYFLAG_QUEST_RECEIVE_CONDS.getValue());
		}
		return attendConditions.verify(player);
	}

	public Map<String, CoreConditions> countryWarPushCond = new HashMap<String, CoreConditions>();

	public CoreConditions getCountryWarPushCond(int id) {
		String key = String.valueOf(id);
		if (!countryWarPushCond.containsKey(key)) {
			ArrayList<String> tmp = STARTEGY_PAOPAO_CONDS.getValue().get(key);
			String[] arrays = new String[tmp.size()];
			tmp.toArray(arrays);
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1, arrays);
			countryWarPushCond.put(key, conditions);
		}
		return countryWarPushCond.get(key);
	}

	private CoreConditions attendDiplomacyRankConds;

	private CoreConditions attendFlagRankConds;

	public CoreConditions getAttendDiplomacyCond() {
		if (attendDiplomacyRankConds == null) {
			attendDiplomacyRankConds = CoreConditionManager.getInstance().getCoreConditions(1,
					KILL_DIPLOMACY_FILTER_CONDS.getValue());
		}
		return attendDiplomacyRankConds;
	}

	public CoreConditions getAttendCountryFlagCond() {
		if (attendFlagRankConds == null) {
			attendFlagRankConds = CoreConditionManager.getInstance().getCoreConditions(1,
					KILL_COUNTRYFLAG_FILTER_CONDS.getValue());
		}
		return attendFlagRankConds;
	}

	public String getCountryFlagBombStoneId(CountryId countryId) {
		return this.COUNTRY_FLAG_BOMBSTONE_SPAWNIDS.getValue().get(Integer.toString(countryId.getValue()));
	}

	public String getCountryDispomacyBombStoneId(CountryId countryId) {
		return this.COUNTRY_DIPLOMACY_BOMBSTONE_SPAWNIDS.getValue().get(Integer.toString(countryId.getValue()));
	}

	public Map<String, String> getCountryNpcHpTipFlag() {
		Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Integer value1 = Integer.parseInt(o1);
				Integer value2 = Integer.parseInt(o2);
				return value2 - value1;
			}

		});
		sortMap.putAll(COUNTRY_NPC_HP_TIPS_FLAG.getValue());
		return sortMap;
	}

	public Map<String, String> getCountryNpcHpTipDiplomacy() {
		Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				Integer value1 = Integer.parseInt(o1);
				Integer value2 = Integer.parseInt(o2);
				return value2 - value1;
			}

		});
		sortMap.putAll(COUNTRY_NPC_HP_TIPS.getValue());
		return sortMap;
	}

	/** 成为储君的条件 */
	@Static("COUNTRY:RESERVEKING_CONDITIONS")
	private ConfigValue<String[]> RESERVEKING_CONDITIONS;

	private CoreConditions reservekingConditions;

	public CoreConditions getReserveKingConditions() {
		if (reservekingConditions == null) {
			reservekingConditions = CoreConditionManager.getInstance().getCoreConditions(1,
					RESERVEKING_CONDITIONS.getValue());
		}
		return reservekingConditions;
	}

	/** 储君使用召集令cd */
	@Static("COUNTRY:RESERVEKING_CALLTOGETHER_CD")
	public ConfigValue<Integer> RESERVEKING_CALLTOGETHER_CD;

	/** 储君离线检查开始cron */
	@Static("COUNTRY:RESERVEKING_BEGIN_CRON")
	public ConfigValue<String> RESERVEKING_BEGIN_CRON;

	/** 储君离线检查结束cron */
	@Static("COUNTRY:RESERVEKING_END_CRON")
	public ConfigValue<String> RESERVEKING_END_CRON;

	/** 储君离线失效时间秒 */
	@Static("COUNTRY:RESERVEKING_OFFLINE_TIME")
	public ConfigValue<Long> RESERVEKING_OFFLINE_TIME;

	/** 各项任务要求完成的次数 */
	@Static("COUNTRY:RESERVEKING_TASK_FINISH_COUNT")
	private ConfigValue<Map<String, Integer>> RESERVEKING_TASK_FINISH_COUNT;

	public Integer getTaskRequestCount(ReserveTaskEnum taskType) {
		return RESERVEKING_TASK_FINISH_COUNT.getValue().get(taskType.getCode() + "");
	}

	/** 储君没有领取奖励的邮件标题 */
	@Static("COUNTRY:RESERVEKING_REWARD_MAIL_TITLE_IL18N")
	public ConfigValue<String> RESERVEKING_REWARD_MAIL_TITLE_IL18N;

	/** 储君没有领取奖励的邮件内容 */
	@Static("COUNTRY:RESERVEKING_REWARD_MAIL_CONTENT_IL18N")
	public ConfigValue<String> RESERVEKING_REWARD_MAIL_CONTENT_IL18N;

	/** 储君任务奖励id */
	@Static("COUNTRY:RESERVEKING_REWARD")
	private ConfigValue<Map<String, String>> RESERVEKING_REWARD;

	@Static("COUNTRY:OFFICIAL_POWER")
	public ConfigValue<Map<String, Integer>> OFFICIAL_POWER;

	public int getOfficialPower(String officialName) {
		Integer ret = OFFICIAL_POWER.getValue().get(officialName);
		return ret == null ? 0 : ret;
	}

	public String getReserveKingTaskRewardId(ReserveTaskEnum taskType) {
		return RESERVEKING_REWARD.getValue().get(taskType.getCode() + "");
	}

	/** 旧储君下台的邮件标题il18n */
	@Static("COUNTRY:RESERVEKING_STEPDOWN_MAIL_TITLE_IL18N")
	public ConfigValue<String> RESERVEKING_STEPDOWN_MAIL_TITLE_IL18N;

	/** 旧储君下台的邮件内容il18n */
	@Static("COUNTRY:RESERVEKING_STEPDOWN_MAIL_CONTENT_IL18N")
	public ConfigValue<String> RESERVEKING_STEPDOWN_MAIL_CONTENT_IL18N;

	/** 新手村地图ID,[11,11,11] */
	@Static("PUBLIC:GREENHAND_MAPS")
	public ConfigValue<Integer[]> GREENHAND_MAPS;

	/** 召唤大B哥的触发器ID */
	@Static("PUBLIC:BIGBROTHER_TRIGGER")
	public ConfigValue<String> BIGBROTHER_TRIGGER;

	/** 没有接任者的名字 */
	@Static("COUNTRY:RESERVEKING_DEFAULT_NEWRESERVEKING_NAME")
	public ConfigValue<String> RESERVEKING_DEFAULT_NEWRESERVEKING_NAME;

	/** 成为储君的消耗 */
	@Static("COUNTRY:RESERVEKING_BECOME_ACTS")
	private ConfigValue<Integer> RESERVEKING_BECOME_ACTS;

	private CurrencyAction reserveKingActs;

	public CurrencyAction getReserveKingBecomeActs() {
		if (reserveKingActs == null) {
			reserveKingActs = CoreActionType.createCurrencyCondition(CurrencyType.GOLD,
					RESERVEKING_BECOME_ACTS.getValue());
		}
		return reserveKingActs;
	}

	@Static("COUNTRY:BEEN_CALLED_BUFF")
	public ConfigValue<Integer> BEEN_CALLED_BUFF;

	/** 众志成城buff属性 */
	@Static("COUNTRY:COUNTRY_UNITY_BUFF")
	private ConfigValue<String> COUNTRY_UNITY_BUFF;

	@Static("COUNTRY:OPENSERVER_COUNTRYFLAG_SPAWN")
	public ConfigValue<Integer> OPENSERVER_COUNTRYFLAG_SPAWN;

	@Static("COUNTRY:OPENSERVER_COUNTRYFLAG_SPECIAL_TIME")
	public ConfigValue<Integer> OPENSERVER_COUNTRYFLAG_SPECIAL_TIME;

	@Static("COUNTRY:OPENSERVER_DIPLOMACY_SPAWN")
	public ConfigValue<Integer> OPENSERVER_DIPLOMACY_SPAWN;

	@Static("COUNTRY:COUNTRYFLAG_SPAWN_CRON")
	public ConfigValue<String[]> COUNTRYFLAG_SPAWN_CRON;

	@Static("COUNTRY:COUNTRYFLAG_BEFORE_SPAWN_CRON")
	public ConfigValue<String[]> COUNTRYFLAG_BEFORE_SPAWN_CRON;

	@Static("COUNTRY:COUNTRYFLAG_QUEST_DURATION")
	public ConfigValue<Integer> COUNTRYFLAG_QUEST_DURATION;

	@Static("COUNTRY:ATTACK_ATTEND_REWARDID")
	public ConfigValue<String> ATTACK_ATTEND_REWARDID;

	@Static("COUNTRY:DEFEND_ATTEND_REWARDID")
	public ConfigValue<String> DEFEND_ATTEND_REWARDID;

	@Static("COUNTRY:ATTEND_REWARD_SCOPE_WIDTH")
	public ConfigValue<Integer> ATTEND_REWARD_SCOPE_WIDTH;

	@Static("COUNTRY:ATTEND_REWARD_SCOPE_HEIGHT")
	public ConfigValue<Integer> ATTEND_REWARD_SCOPE_HEIGHT;

	@Static("COUNTRY:FLAG_ATTACK_FAIL_REWARDID")
	public ConfigValue<String> FLAG_ATTACK_FAIL_REWARDID;

	@Static("COUNTRY:FLAG_DEFEND_FAIL_REWARDID")
	public ConfigValue<String> FLAG_DEFEND_FAIL_REWARDID;

	@Static("COUNTRY:COUNTRYFLAG_QUEST_CHOOSERGROUP")
	public ConfigValue<String> COUNTRYFLAG_QUEST_CHOOSERGROUP;

	@Static("COUNTRY:COUNTRYFLAG_QUEST_FIRST_CHOOSERGROUP")
	public ConfigValue<String> COUNTRYFLAG_QUEST_FIRST_CHOOSERGROUP;

	/** 大臣的定时刷新cron表达式 */
	@Static("COUNTRY:COUNTRY_DIPLOMACY_REFRESH_TIME_CRONS")
	public ConfigValue<String[]> COUNTRY_DIPLOMACY_REFRESH_TIME_CRONS;

	@Static("COUNTRY:COUNTRY_FLAG_ATTEND_SKILL")
	public ConfigValue<Integer> COUNTRY_FLAG_ATTEND_SKILL;

	@Static("COUNTRY:CUT_ATTEND_MAIL_TITLE")
	public ConfigValue<String> CUT_ATTEND_MAIL_TITLE;

	@Static("COUNTRY:CUT_ATTEND_MAIL_CONTENT")
	public ConfigValue<String> CUT_ATTEND_MAIL_CONTENT;

	@Static("COUNTRY:DEFEND_ATTEND_MAIL_CONTENT")
	public ConfigValue<String> DEFEND_ATTEND_MAIL_CONTENT;

	@Static("COUNTRY:DEFEND_ATTEND_MAIL_TITLE")
	public ConfigValue<String> DEFEND_ATTEND_MAIL_TITLE;

	@Static("COUNTRY:CUT_ATTEND_REWARD")
	public ConfigValue<String> CUT_ATTEND_REWARD;

	@Static("COUNTRY:DEFEND_LOST_REWARD")
	public ConfigValue<String> DEFEND_LOST_REWARD;

	@Static("COUNTRY:COMPENSATE_LEFT_SECOND_COND")
	public ConfigValue<Integer> COMPENSATE_LEFT_SECOND_COND;

	private Stat[][] countryUnityBuff;

	public Stat[][] getCountryUnityBuff() {
		if (countryUnityBuff == null) {
			countryUnityBuff = JsonUtils.string2Array(COUNTRY_UNITY_BUFF.getValue(), Stat[].class);
		}
		return countryUnityBuff;
	}

	public long getNextCountryFlagStartTime() {
		return getNextCountryFlagStartTime(new Date());
	}

	public long getNextCountryFlagStartTime(Date now) {
		long min = Long.MAX_VALUE;
		long nextTime = 0l;
		for (String cron : COUNTRYFLAG_SPAWN_CRON.getValue()) {
			nextTime = DateUtils.getNextTime(cron, now).getTime();
			if (nextTime - now.getTime() < min) {
				min = nextTime - now.getTime();
			}
		}

		return min + now.getTime();
	}

	public long getNextCountryDiplomacyStartTime(Date now) {
		long min = Long.MAX_VALUE;
		long nextTime = 0l;
		for (String cron : COUNTRY_DIPLOMACY_REFRESH_TIME_CRONS.getValue()) {
			nextTime = DateUtils.getNextTime(cron, now).getTime();
			if (nextTime - now.getTime() < min) {
				min = nextTime - now.getTime();
			}
		}

		return min + now.getTime();
	}

	public long getNextCountryDiplomacyStartTime() {
		return getNextCountryDiplomacyStartTime(new Date());
	}

	/** 被召集的卫队条件 */
	@Static("COUNTRY:GUARD_CALLED_CONDS")
	private ConfigValue<String[]> GUARD_CALLED_CONDS;

	private CoreConditions guardCalledCondtions;

	public CoreConditions getGuardCalledCondtions() {
		if (guardCalledCondtions == null) {
			guardCalledCondtions = CoreConditionManager.getInstance().getCoreConditions(1,
					GUARD_CALLED_CONDS.getValue());
		}
		return guardCalledCondtions;
	}

	@Static("COUNTRY:CALLED_SPECIAL_MAPID")
	public ConfigValue<Integer> CALLED_SPECIAL_MAPID;

	@Static("COUNTRY:CALLED_SPECIAL_CONDS_ONE")
	private ConfigValue<String[]> CALLED_SPECIAL_CONDS_ONE;

	@Static("COUNTRY:CALLED_SPECIAL_CONDS_TWO")
	private ConfigValue<String[]> CALLED_SPECIAL_CONDS_TWO;

	@Static("COUNTRY:COUNTRYFLAG_NOT_VISIBLENPC")
	public ConfigValue<Map<String, ArrayList<String>>> COUNTRYFLAG_NOT_VISIBLENPC;

	@Static("AUTHORITY:COUNTRY_CALL_PEOPLE")
	public ConfigValue<String[]> COUNTRY_CALL_PEOPLE;

	@Static("COUNTRY:TRAITOR_DURATION_TIME_SECOND")
	public ConfigValue<Integer> TRAITOR_DURATION_TIME_SECOND;

	private CoreConditions calledSpecialCondsOne;

	private CoreConditions calledSpecialCondsTwo;

	public CoreConditions getCalledSpecialCondsOne() {
		if (calledSpecialCondsOne == null) {
			calledSpecialCondsOne = CoreConditionManager.getInstance().getCoreConditions(1,
					CALLED_SPECIAL_CONDS_ONE.getValue());
		}
		return calledSpecialCondsOne;
	}

	public CoreConditions getCalledSpecialCondsTwo() {
		if (calledSpecialCondsTwo == null) {
			calledSpecialCondsTwo = CoreConditionManager.getInstance().getCoreConditions(1,
					CALLED_SPECIAL_CONDS_TWO.getValue());
		}
		return calledSpecialCondsTwo;
	}

	/** 国家科技最大建设总值 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_MAX_BUILDVALUE")
	public ConfigValue<Integer> COUNTRY_TECHNOLOGY_MAX_BUILDVALUE;

	/** 捐献奖励 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_DONATE_REWARDID")
	public ConfigValue<String> COUNTRY_TECHNOLOGY_DONATE_REWARDID;

	/** 国家科技国旗初始数量 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_FLAG_INIT_COUNT")
	public ConfigValue<Integer> COUNTRY_TECHNOLOGY_FLAG_INIT_COUNT;

	/** 国家科技国旗最大存储数量 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_FLAG_MAX_COUNT")
	public ConfigValue<Integer> COUNTRY_TECHNOLOGY_FLAG_MAX_COUNT;

	/** 增加军旗的时间间隔（秒） */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_FLAG_CREATE_TIME")
	public ConfigValue<Long> COUNTRY_TECHNOLOGY_FLAG_CREATE_TIME;

	/** 国家建设令道具id */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_BUILD_ITEM_ID")
	public ConfigValue<String> COUNTRY_TECHNOLOGY_BUILD_ITEM_ID;

	/** 军旗放置条件 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_FLAG_PLACE_CONDS")
	private ConfigValue<String[]> COUNTRY_TECHNOLOGY_FLAG_PLACE_CONDS;

	private CoreConditions flagPlaceConds;

	public CoreConditions getTechnologyFlagPlaceConds() {
		if (null == flagPlaceConds) {
			flagPlaceConds = CoreConditionManager.getInstance().getCoreConditions(1,
					COUNTRY_TECHNOLOGY_FLAG_PLACE_CONDS.getValue());
		}
		return flagPlaceConds;
	}

	/** 军旗spawnid的选择器 */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_CHOOSER_GROUPID")
	public ConfigValue<String> COUNTRY_TECHNOLOGY_CHOOSER_GROUPID;

	/** 军旗消失时间间隔（秒） */
	@Static("COUNTRY:COUNTRY_TECHNOLOGY_FLAG_PERIOD")
	public ConfigValue<Long> COUNTRY_TECHNOLOGY_FLAG_PERIOD;

	@Static("COUNTRY:COUNTRY_TACHNOLOGY_BASIC_RANDOM")
	public ConfigValue<Integer[]> COUNTRY_TACHNOLOGY_BASIC_RANDOM;

	@Static("COUNTRY:COUNTRY_TECH_ACT_EXTRA_ANNOUNCE")
	public ConfigValue<Integer[]> COUNTRY_TECH_ACT_EXTRA_ANNOUNCE;

	@Static("COUNTRY:COUNTRY_TECH_ACT_CD")
	public ConfigValue<Integer> COUNTRY_TECH_ACT_CD;

	public int getCountryTechCount(Country country) {
		return COUNTRY_TECH_ACT_EXTRA_ANNOUNCE.getValue()[country.getNewTechnology().getGrade()];
	}

	@Static("COUNTRY:COUNTRY_TECHNOLODY_FLAG_SKILLID")
	public ConfigValue<Integer> COUNTRY_TECHNOLODY_FLAG_SKILLID;

	@Static("COUNTRY:COUNTRY_TECH_BUILDVALUE_CHECK_DAYS")
	public ConfigValue<Integer> COUNTRY_TECH_BUILDVALUE_CHECK_DAYS;

	@Static("COUNTRY:COUNTRY_TECH_MAX_STAND_BUILDVALUE")
	public ConfigValue<Integer> COUNTRY_TECH_MAX_STAND_BUILDVALUE;

}
