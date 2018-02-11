package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.boss.BossRankCondition;
import com.mmorpg.mir.model.core.condition.country.CountryCalltogeterCountCondition;
import com.mmorpg.mir.model.core.condition.country.CountryCondition;
import com.mmorpg.mir.model.core.condition.country.CountryCurrencyCondition;
import com.mmorpg.mir.model.core.condition.country.CountryFactoryLevelCondition;
import com.mmorpg.mir.model.core.condition.country.CountryIsWeakCondition;
import com.mmorpg.mir.model.core.condition.country.CountryLessGuardCountCondition;
import com.mmorpg.mir.model.core.condition.country.CountryMissionFinishCountCondition;
import com.mmorpg.mir.model.core.condition.country.CountryNotForbidChatCondition;
import com.mmorpg.mir.model.core.condition.country.CountryNotOfficalCondition;
import com.mmorpg.mir.model.core.condition.country.CountryOfficalCondition;
import com.mmorpg.mir.model.core.condition.country.CountryOfficalCountCondition;
import com.mmorpg.mir.model.core.condition.country.CountryShopLevelCondition;
import com.mmorpg.mir.model.core.condition.country.CountryShopLimitCondition;
import com.mmorpg.mir.model.core.condition.country.CountryTechnologyBuildValueHLRateCondtion;
import com.mmorpg.mir.model.core.condition.country.CountryTechnologyGradeCondition;
import com.mmorpg.mir.model.core.condition.country.CountryTodayAllAuthorityCountCondition;
import com.mmorpg.mir.model.core.condition.country.CountryTodayAuthorityCountCondition;
import com.mmorpg.mir.model.core.condition.country.NotLocalCountryCondition;
import com.mmorpg.mir.model.core.condition.country.PlayerIsOfficial;
import com.mmorpg.mir.model.core.condition.country.PlayerIsReserveKing;
import com.mmorpg.mir.model.core.condition.drop.HuntMonsterCountCondition;
import com.mmorpg.mir.model.core.condition.drop.HuntMonstersCountCondition;
import com.mmorpg.mir.model.core.condition.drop.ItemDropCountCondition;
import com.mmorpg.mir.model.core.condition.drop.MonsterDropCountCondition;
import com.mmorpg.mir.model.core.condition.drop.MonstersDropCountCondition;
import com.mmorpg.mir.model.core.condition.drop.PlayerHuntMonsterCountCondition;
import com.mmorpg.mir.model.core.condition.drop.PlayerItemDropCountCondition;
import com.mmorpg.mir.model.core.condition.gangofwar.GangOfWarAttackDefendCondition;
import com.mmorpg.mir.model.core.condition.gangofwar.GangOfWarFightingCondition;
import com.mmorpg.mir.model.core.condition.gangofwar.GangOfWarWinCondition;
import com.mmorpg.mir.model.core.condition.kingofwar.KingOfWarFightingCondition;
import com.mmorpg.mir.model.core.condition.kingofwar.KingOfWarRankCondition;
import com.mmorpg.mir.model.core.condition.kingofwar.KingOfWarReliveStatusNpcCondition;
import com.mmorpg.mir.model.core.condition.map.MapCopyEncourageCountCondition;
import com.mmorpg.mir.model.core.condition.map.MapLessCreateTimeCondition;
import com.mmorpg.mir.model.core.condition.map.MapMoreCreateTimeCondition;
import com.mmorpg.mir.model.core.condition.openactive.OpenActiveCompeteCondition;
import com.mmorpg.mir.model.core.condition.openactive.OpenActiveConsumeCondition;
import com.mmorpg.mir.model.core.condition.openactive.OpenActiveEnhanceCondition;
import com.mmorpg.mir.model.core.condition.openactive.OpenActiveHorseCondition;
import com.mmorpg.mir.model.core.condition.quest.AcceptQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.CompleteNotQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.CompleteQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.CompleteQuestORCondition;
import com.mmorpg.mir.model.core.condition.quest.CurrentNoAcceptTypeQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.CurrentNoQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestCreateMoreTimeCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestKeyCtxCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestKeyValueCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestNotCompleteCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestNotCreateTodayCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestNotInCopyCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestPhaseCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestStarCondition;
import com.mmorpg.mir.model.core.condition.quest.RandomQuestCDCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayAcceptQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteCopyQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteNotQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteNotRescueQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteRescueQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayCompleteTypeCountCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayNoCompleteTypeQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.TodayQuestCompleteByCountryCondition;
import com.mmorpg.mir.model.core.condition.rp.BothNotTraitor;
import com.mmorpg.mir.model.core.condition.rp.LevelGapCondition;
import com.mmorpg.mir.model.core.condition.rp.PlayerInWeakCondition;
import com.mmorpg.mir.model.core.condition.rp.PlayerIsTraitorCondition;
import com.mmorpg.mir.model.core.condition.rp.RPNotOverLimitCondition;
import com.mmorpg.mir.model.core.condition.trigger.NpcMoreCreateTimeCondition;
import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.purse.model.CurrencyType;

public enum CoreConditionType {

	HP {
		@SuppressWarnings("unchecked")
		@Override
		protected HpCondition create() {
			return new HpCondition();
		}

	},
	LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected LevelCondition create() {
			return new LevelCondition();
		}

	},
	SKILL_MP {
		@SuppressWarnings("unchecked")
		@Override
		protected SkillMpCondition create() {
			return new SkillMpCondition();
		}

	},
	SKILL_DP {
		@SuppressWarnings("unchecked")
		@Override
		protected SkillDpCondition create() {
			return new SkillDpCondition();
		}

	},

	PACK {
		@SuppressWarnings("unchecked")
		@Override
		protected PackCondition create() {
			return new PackCondition();
		}

	},

	ITEM {
		@SuppressWarnings("unchecked")
		@Override
		protected ItemCondition create() {
			return new ItemCondition();
		}
	},

	HPNOTFULL {
		@SuppressWarnings("unchecked")
		@Override
		protected HpNotFullCondition create() {
			return new HpNotFullCondition();
		}
	},

	DPNOTFULL {
		@SuppressWarnings("unchecked")
		@Override
		protected DpNotFullCondition create() {
			return new DpNotFullCondition();
		}
	},

	MPNOTFULL {
		@SuppressWarnings("unchecked")
		@Override
		protected MpNotFullCondition create() {
			return new MpNotFullCondition();
		}
	},
	BETWEEN_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected BetweenTimeCondition create() {
			return new BetweenTimeCondition();
		}
	},
	BETWEEN_CRON_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected BetweenCronTimeCondition create() {
			return new BetweenCronTimeCondition();
		}
	},
	COUNTRY_OFFICAL {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryOfficalCondition create() {
			return new CountryOfficalCondition();
		}
	},
	COUNTRY_TODAY_AUTHORITY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryTodayAuthorityCountCondition create() {
			return new CountryTodayAuthorityCountCondition();
		}
	},
	PLAYER_ITEM_DROP_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerItemDropCountCondition create() {
			return new PlayerItemDropCountCondition();
		}
	},
	PLAYER_MONSTER_HUNT_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerHuntMonsterCountCondition create() {
			return new PlayerHuntMonsterCountCondition();
		}
	},
	MONSTER_DROP_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected MonsterDropCountCondition create() {
			return new MonsterDropCountCondition();
		}
	},
	DROP_COUNT_MONSTERS {
		@SuppressWarnings("unchecked")
		@Override
		protected MonstersDropCountCondition create() {
			return new MonstersDropCountCondition();
		}
	},
	PLAYER_OPERATOR {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerOperatorCondition create() {
			return new PlayerOperatorCondition();
		}
	},
	PLAYER_NOT_OPERATOR {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotOperatorCondition create() {
			return new PlayerNotOperatorCondition();
		}
	},
	ITEM_DROP_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected ItemDropCountCondition create() {
			return new ItemDropCountCondition();
		}
	},
	MONSTER_HUNT_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected HuntMonsterCountCondition create() {
			return new HuntMonsterCountCondition();
		}
	},
	HUNT_MONSTERS_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected HuntMonstersCountCondition create() {
			return new HuntMonstersCountCondition();
		}
	},
	COUNTRY_TODAY_ALL_AUTHORITY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryTodayAllAuthorityCountCondition create() {
			return new CountryTodayAllAuthorityCountCondition();
		}
	},
	COUNTRY_NOT_OFFICAL {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryNotOfficalCondition create() {
			return new CountryNotOfficalCondition();
		}
	},

	/** 最多响应X个玩家，当超过X个玩家点击召集令后，其他玩家需要排 */
	COUNTRY_CALL_MAX_NUM {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryCallMaxNumCondition create() {
			return new CountryCallMaxNumCondition();
		}
	},

	DISTRIBUTETOGETHERTOKEN {
		@SuppressWarnings("unchecked")
		@Override
		protected DistributeTogetherTokenCondition create() {
			return new DistributeTogetherTokenCondition();
		}
	},
	COUNTRY_OFFICAL_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryOfficalCountCondition create() {
			return new CountryOfficalCountCondition();
		}
	},
	COUNTRY_SHOP_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryShopLimitCondition create() {
			return new CountryShopLimitCondition();
		}
	},
	MAP_MORE_CREATETIME {
		@SuppressWarnings("unchecked")
		@Override
		protected MapMoreCreateTimeCondition create() {
			return new MapMoreCreateTimeCondition();
		}
	},
	MAP_LESS_CREATETIME {
		@SuppressWarnings("unchecked")
		@Override
		protected MapLessCreateTimeCondition create() {
			return new MapLessCreateTimeCondition();
		}
	},
	MAP_COPY_ENCOURAGECOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected MapCopyEncourageCountCondition create() {
			return new MapCopyEncourageCountCondition();
		}
	},
	KOW_RELIVE_STATUS {
		@SuppressWarnings("unchecked")
		@Override
		protected KingOfWarReliveStatusNpcCondition create() {
			return new KingOfWarReliveStatusNpcCondition();
		}
	},
	KOW_RANK {
		@SuppressWarnings("unchecked")
		@Override
		protected KingOfWarRankCondition create() {
			return new KingOfWarRankCondition();
		}
	},
	KOW_FIGHTING {
		@SuppressWarnings("unchecked")
		@Override
		protected KingOfWarFightingCondition create() {
			return new KingOfWarFightingCondition();
		}
	},
	FOREVERTRUE {
		@SuppressWarnings("unchecked")
		@Override
		protected ForeverTrueCondition create() {
			return new ForeverTrueCondition();
		}
	},
	PLAYER_ID {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerIdCondition create() {
			return new PlayerIdCondition();
		}
	},
	PLAYER_NAME {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNameCondition create() {
			return new PlayerNameCondition();
		}
	},
	PLAYER_NOT_DIE {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotDieCondition create() {
			return new PlayerNotDieCondition();
		}
	},
	MAP {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerMapCondition create() {
			return new PlayerMapCondition();
		}
	},
	MAPXY {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerMapXYCondition create() {
			return new PlayerMapXYCondition();
		}
	},
	FOREVERFALSE {
		@SuppressWarnings("unchecked")
		@Override
		protected ForeverFalseCondition create() {
			return new ForeverFalseCondition();
		}
	},
	COPY_ENTER_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyEnterCountCondition create() {
			return new CopyEnterCountCondition();
		}
	},
	COPY_ENTER_COUNT_EX {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyEnterCountExCondition create() {
			return new CopyEnterCountExCondition();
		}
	},
	COPY_TODAY_NOT_COMPLETE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyTodayNotCompleteCountCondition create() {
			return new CopyTodayNotCompleteCountCondition();
		}
	},
	COPY_ALL_COMPLETED_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyCompleteCountCondition create() {
			return new CopyCompleteCountCondition();
		}
	},
	COPY_LADDER_RESET_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyLadderResetCountCondition create() {
			return new CopyLadderResetCountCondition();
		}
	},
	COPY_NOT_ALL_COMPLETED_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyNotCompleteCountCondition create() {
			return new CopyNotCompleteCountCondition();
		}
	},
	COPY_ENTER_TYPE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyEnterTypeCountCondition create() {
			return new CopyEnterTypeCountCondition();
		}
	},
	COPY_LADDER_COMPLETE {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyLadderCompleteCondition create() {
			return new CopyLadderCompleteCondition();
		}
	},
	COPY_ENTER_CD {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyEnterCDCondition create() {
			return new CopyEnterCDCondition();
		}
	},
	COPY_TYPE_ENTER_CD {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyTypeEnterCDCondition create() {
			return new CopyTypeEnterCDCondition();
		}

	},
	COPY_BUY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyBuyCountCondition create() {
			return new CopyBuyCountCondition();
		}
	},
	COPY_BUY_TYPE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CopyTypeBuyCountCondition create() {
			return new CopyTypeBuyCountCondition();
		}
	},
	PLAYER_IN_INCOPY {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerInCopyCondition create() {
			return new PlayerInCopyCondition();
		}
	},
	PLAYER_IN_GANG {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerInGangCondition create() {
			return new PlayerInGangCondition();
		}
	},
	NPC_CREATE_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected NpcMoreCreateTimeCondition create() {
			return new NpcMoreCreateTimeCondition();
		}
	},
	NPC_LESS_CREATE_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected NpcLessCreateTimeCondition create() {
			return new NpcLessCreateTimeCondition();
		}
	},
	ACCEPT_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected AcceptQuestCondition create() {
			return new AcceptQuestCondition();
		}
	},
	ROLETYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected RoleTypeCondition create() {
			return new RoleTypeCondition();
		}
	},
	COMPLETE_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected CompleteQuestCondition create() {
			return new CompleteQuestCondition();
		}
	},
	COMPLETE_QUEST_OR {
		@SuppressWarnings("unchecked")
		@Override
		protected CompleteQuestORCondition create() {
			return new CompleteQuestORCondition();
		}
	},
	CURRENT_NO_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected CurrentNoQuestCondition create() {
			return new CurrentNoQuestCondition();
		}
	},
	CURRENT_HAS_QUEST_COMPLETE {

		@SuppressWarnings("unchecked")
		@Override
		protected CurrentQuestHasCompleteCondition create() {
			return new CurrentQuestHasCompleteCondition();
		}

	},
	NOCOMPLETE_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected CompleteNotQuestCondition create() {
			return new CompleteNotQuestCondition();
		}
	},
	TODAY_NOCOMPLETE_RESCUE_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteNotRescueQuestCondition create() {
			return new TodayCompleteNotRescueQuestCondition();
		}
	},
	QUEST_KEYCTX {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestKeyCtxCondition create() {
			return new QuestKeyCtxCondition();
		}
	},
	QUEST_CREATIME {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestCreateMoreTimeCondition create() {
			return new QuestCreateMoreTimeCondition();
		}
	},
	QUEST_KEYVALUE {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestKeyValueCondition create() {
			return new QuestKeyValueCondition();
		}
	},
	QUEST_PHASE {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestPhaseCondition create() {
			return new QuestPhaseCondition();
		}
	},
	QUEST_NOTTODAYCREATE {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestNotCreateTodayCondition create() {
			return new QuestNotCreateTodayCondition();
		}
	},
	PLAYER_NOT_INCOPY {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestNotInCopyCondition create() {
			return new QuestNotInCopyCondition();
		}
	},
	TODAY_ACCEPTQUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayAcceptQuestCondition create() {
			return new TodayAcceptQuestCondition();
		}
	},
	RANDOM_QUEST_CD {
		@SuppressWarnings("unchecked")
		@Override
		protected RandomQuestCDCondition create() {
			return new RandomQuestCDCondition();
		}
	},
	TODAY_COMPLETEQUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteQuestCondition create() {
			return new TodayCompleteQuestCondition();
		}
	},
	TODAY_COMPLETE_COPYQUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteCopyQuestCondition create() {
			return new TodayCompleteCopyQuestCondition();
		}
	},
	TODAY_NOCOMPLETEQUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteNotQuestCondition create() {
			return new TodayCompleteNotQuestCondition();
		}
	},
	QUEST_STAR {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestStarCondition create() {
			return new QuestStarCondition();
		}
	},
	TODAY_NOCOMPLETE_QUESTTYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayNoCompleteTypeQuestCondition create() {
			return new TodayNoCompleteTypeQuestCondition();
		}
	},
	NOCOMPLETE_QUESTTYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected NoCompleteTypeQuestCondition create() {
			return new NoCompleteTypeQuestCondition();
		}
	},
	TODAY_COMPLETE_QUESTTYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteTypeCountCondition create() {
			return new TodayCompleteTypeCountCondition();
		}
	},
	TODAY_COMPLETE_RESCUE_QUEST {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayCompleteRescueQuestCondition create() {
			return new TodayCompleteRescueQuestCondition();
		}
	},
	CURRENT_NOT_ACCEPT_QUESTTYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected CurrentNoAcceptTypeQuestCondition create() {
			return new CurrentNoAcceptTypeQuestCondition();
		}
	},
	SKILL_REWARDID {
		@SuppressWarnings("unchecked")
		@Override
		protected SkillRewardIdCondition create() {
			return new SkillRewardIdCondition();
		}
	},
	STATUS_NPC {
		@SuppressWarnings("unchecked")
		@Override
		protected StatusNpcStatusCondition create() {
			return new StatusNpcStatusCondition();
		}
	},
	TODAY_RECHARGE {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayRechargeCondition create() {
			return new TodayRechargeCondition();
		}
	},
	SKILL_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected SkillLevelCondition create() {
			return new SkillLevelCondition();
		}
	},
	SKILL_END {
		@SuppressWarnings("unchecked")
		@Override
		protected SkillEndCondition create() {
			return new SkillEndCondition();
		}
	},
	CURRENCY {
		@SuppressWarnings("unchecked")
		@Override
		protected CurrencyCondition create() {
			return new CurrencyCondition();
		}
	},
	COUNTRY_CURRENCY {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryCurrencyCondition create() {
			return new CountryCurrencyCondition();
		}
	},
	COUNTRY_GUARD_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryLessGuardCountCondition create() {
			return new CountryLessGuardCountCondition();
		}
	},
	COUNTRY_NOT_FORBIDCHAT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryNotForbidChatCondition create() {
			return new CountryNotForbidChatCondition();
		}
	},
	GANGOFWAR_ATTACK_DEFEND {
		@SuppressWarnings("unchecked")
		@Override
		protected GangOfWarAttackDefendCondition create() {
			return new GangOfWarAttackDefendCondition();
		}
	},
	GANGOFWAR_FIGHTING {
		@SuppressWarnings("unchecked")
		@Override
		protected GangOfWarFightingCondition create() {
			return new GangOfWarFightingCondition();
		}
	},
	GANGOFWAR_WIN {
		@SuppressWarnings("unchecked")
		@Override
		protected GangOfWarWinCondition create() {
			return new GangOfWarWinCondition();
		}
	},
	/** 国民被召集次数限制 */
	COUNTRY_CALLTOGETHER_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryCalltogeterCountCondition create() {
			return new CountryCalltogeterCountCondition();
		}
	},
	BOSS_RANK {
		@SuppressWarnings("unchecked")
		@Override
		protected BossRankCondition create() {
			return new BossRankCondition();
		}
	},
	COUNTRY_FACTORY_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryFactoryLevelCondition create() {
			return new CountryFactoryLevelCondition();
		}
	},
	CURRENCY_TOTAL {
		@SuppressWarnings("unchecked")
		@Override
		protected CurrencyTotalCondition create() {
			return new CurrencyTotalCondition();
		}
	},
	ITEM_VIP_DAILY_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected ItemDailyLimitVipCondition create() {
			return new ItemDailyLimitVipCondition();
		}
	},
	ITEM_TOTAL_DAILY_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected ItemDailyLimitCondition create() {
			return new ItemDailyLimitCondition();
		}
	},
	ITEM_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected ItemLimitCondition create() {
			return new ItemLimitCondition();
		}
	},
	RP_NOTOVER_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected RPNotOverLimitCondition create() {
			return new RPNotOverLimitCondition();
		}
	},
	LEVEL_GAP {
		@SuppressWarnings("unchecked")
		@Override
		protected LevelGapCondition create() {
			return new LevelGapCondition();
		}
	},
	IN_WEAK {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerInWeakCondition create() {
			return new PlayerInWeakCondition();
		}
	},
	NOT_IN_WEAK {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotInWeakCondition create() {
			return new PlayerNotInWeakCondition();
		}
	},
	PLAYER_IS_TRAITOR {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerIsTraitorCondition create() {
			return new PlayerIsTraitorCondition();
		}
	},
	BOTH_NOT_TRAITOR {
		@SuppressWarnings("unchecked")
		@Override
		protected BothNotTraitor create() {
			return new BothNotTraitor();
		}
	},
	ACTIVITY_IN_PERIOD {
		@SuppressWarnings("unchecked")
		@Override
		protected ActivityInPeriodCondition create() {
			return new ActivityInPeriodCondition();
		}
	},
	IS_TRAITOR {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerIsTraitorCondition create() {
			return new PlayerIsTraitorCondition();
		}
	},
	MILITARY_RANK {
		@SuppressWarnings("unchecked")
		@Override
		protected MilitaryRankCondition create() {
			return new MilitaryRankCondition();
		}
	},
	COUNTRY_COND {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryCondition create() {
			return new CountryCondition();
		}
	},
	NOT_IN_TRDING {
		@SuppressWarnings("unchecked")
		@Override
		protected NotInTradingCondition create() {
			return new NotInTradingCondition();
		}
	},
	HAS_SOUL_STAT {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipmentHasSoulCondition create() {
			return new EquipmentHasSoulCondition();
		}
	},
	LORRY_BEEN_ROBBERY {
		@SuppressWarnings("unchecked")
		@Override
		protected LorryBeenRobberyCondition create() {
			return new LorryBeenRobberyCondition();
		}
	},
	LORRY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected LorryCountCondition create() {
			return new LorryCountCondition();
		}
	},
	LORRY_COLOR_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected LorryColorTotalCountCondition create() {
			return new LorryColorTotalCountCondition();
		}

	},
	LORRY_HISTORY_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected LorryCompleteHistoryCountCondition create() {
			return new LorryCompleteHistoryCountCondition();
		}

	},
	PLAYER_ALREADY_DEAD {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerDeadCondition create() {
			return new PlayerDeadCondition();
		}
	},
	SAME_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipSameLevelCondition create() {
			return new EquipSameLevelCondition();
		}
	},
	SAME_ELEMENT {
		@SuppressWarnings("unchecked")
		@Override
		protected SameElementCondition create() {
			return new SameElementCondition();
		}
	},
	ATTACK_EQUIPMENT {
		@SuppressWarnings("unchecked")
		@Override
		protected BelongToAttackEquipCondition create() {
			return new BelongToAttackEquipCondition();
		}
	},
	DEFENSE_EQUIPMENT {
		@SuppressWarnings("unchecked")
		@Override
		protected BelongToDefenseEquipCondition create() {
			return new BelongToDefenseEquipCondition();
		}
	},
	EQUIP_TYPE_CONDITION {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipmentTypeCondition create() {
			return new EquipmentTypeCondition();
		}
	},
	NOT_LOCAL_RESIDENT {
		@SuppressWarnings("unchecked")
		@Override
		protected NotLocalCountryCondition create() {
			return new NotLocalCountryCondition();
		}
	},
	ACTIVATESOUL {
		@SuppressWarnings("unchecked")
		@Override
		protected ActivateSoulCondition create() {
			return new ActivateSoulCondition();
		}
	},
	HAS_SPECIFIC_EFFECT {
		@SuppressWarnings("unchecked")
		@Override
		protected HasSpecificEffectCondition create() {
			return new HasSpecificEffectCondition();
		}
	},
	IS_OFFICIAL {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerIsOfficial create() {
			return new PlayerIsOfficial();
		}
	},
	MODULE_OPEN {
		@SuppressWarnings("unchecked")
		@Override
		protected ModuleOpenCondition create() {
			return new ModuleOpenCondition();
		}
	},
	HORSE_GRADE {
		@SuppressWarnings("unchecked")
		@Override
		protected HorseGradeCondition create() {
			return new HorseGradeCondition();
		}
	},
	MONSTER_AROUND_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected MonsterAroundCondition create() {
			return new MonsterAroundCondition();
		}
	},
	HORSE_NOT_LEARN_SKILL {
		@SuppressWarnings("unchecked")
		@Override
		protected HorseNotLearnSKillCondition create() {
			return new HorseNotLearnSKillCondition();
		}
	},
	ARTIFACT_GRADE {
		@SuppressWarnings("unchecked")
		@Override
		protected ArtifactGradeCondition create() {
			return new ArtifactGradeCondition();
		}
	},
	SOUL_GRADE {
		@SuppressWarnings("unchecked")
		@Override
		protected SoulGradeCondition create() {
			return new SoulGradeCondition();
		}
	},
	STRATEGY_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected MilitaryStrategyLevelCondition create() {
			return new MilitaryStrategyLevelCondition();
		}
	},
	EQUIP_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected HasEnhancedEquipCondition create() {
			return new HasEnhancedEquipCondition();
		}
	},
	EQUIP_TOTAL_ENHANCED {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipEnhanceNumCondition create() {
			return new EquipEnhanceNumCondition();
		}
	},
	EQUIP_QUALITY {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipQualityNumsCondition create() {
			return new EquipQualityNumsCondition();
		}
	},
	EQUIP_SOUL {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipSouledCondition create() {
			return new EquipSouledCondition();
		}
	},
	EQUIP_SOUL_SUIT {
		@SuppressWarnings("unchecked")
		@Override
		protected EquipSoulSuitCondition create() {
			return new EquipSoulSuitCondition();
		}
	},
	COUNTRY_EXPRESS {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryExpressTimeCondition create() {
			return new CountryExpressTimeCondition();
		}
	},
	COUNTRY_TEMPLE {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryTempleTimeCondition create() {
			return new CountryTempleTimeCondition();
		}
	},
	VIP_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected VipLevelCondition create() {
			return new VipLevelCondition();
		}
	},
	NOT_IN_ENEMY_COUNTRY {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotInEnemyCountryCondition create() {
			return new PlayerNotInEnemyCountryCondition();
		}
	},
	NOT_IN_ENEMY_HOME {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotInEnemyHomeCondition create() {
			return new PlayerNotInEnemyHomeCondition();
		}
	},
	GIFT_NOT_RECIEVED {
		@SuppressWarnings("unchecked")
		@Override
		protected GiftNotRecievedCondition create() {
			return new GiftNotRecievedCondition();
		}
	},
	GIFT_RECIEVED {
		@SuppressWarnings("unchecked")
		@Override
		protected GiftRecievedCondition create() {
			return new GiftRecievedCondition();
		}
	},
	CREATURE_LIFE {
		@SuppressWarnings("unchecked")
		@Override
		protected LifeCondition create() {
			return new LifeCondition();
		}
	},
	PUBLIC_COOLDOWN {
		@SuppressWarnings("unchecked")
		@Override
		protected PublicCoolDownCondition create() {
			return new PublicCoolDownCondition();
		}
	},
	PROMOTION_STAGE {
		@SuppressWarnings("unchecked")
		@Override
		protected PromotionStageCondition create() {
			return new PromotionStageCondition();
		}
	},
	BOSS_COPY_NOT_REWARD {
		@SuppressWarnings("unchecked")
		@Override
		protected BossCopyNotRewardCondition create() {
			return new BossCopyNotRewardCondition();
		}
	},
	BOSS_COPY_FIRST_DONE {
		@SuppressWarnings("unchecked")
		@Override
		protected BossCopyFirstDoneCondition create() {
			return new BossCopyFirstDoneCondition();
		}
	},
	BOSS_COPY_NOT_FIRST_DONE {
		@SuppressWarnings("unchecked")
		@Override
		protected BossCopyNotFirstDoneCondition create() {
			return new BossCopyNotFirstDoneCondition();
		}
	},
	LOGIN_TYPE {
		@SuppressWarnings("unchecked")
		@Override
		protected LoginTypeCondition create() {
			return new LoginTypeCondition();
		}
	},
	QUEST_NOT_COMPLETE {
		@SuppressWarnings("unchecked")
		@Override
		protected QuestNotCompleteCondition create() {
			return new QuestNotCompleteCondition();
		}
	},
	TODAY_COMPLETE_COUNTRY {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayQuestCompleteByCountryCondition create() {
			return new TodayQuestCompleteByCountryCondition();
		}
	},
	KINGOFKING {
		@SuppressWarnings("unchecked")
		@Override
		protected KingOfKingCondition create() {
			return new KingOfKingCondition();
		}
	},
	LEVEL_BEFORE_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected LevelBeforeTimeCondition create() {
			return new LevelBeforeTimeCondition();
		}
	},
	COUNTRY_SHOP_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryShopLevelCondition create() {
			return new CountryShopLevelCondition();
		}
	},
	KILLING_ITEM {
		@SuppressWarnings("unchecked")
		@Override
		protected KillingItemCondition create() {
			return new KillingItemCondition();
		}
	},
	TODAY_WARSHIP_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected TodayWarshipCountCondition create() {
			return new TodayWarshipCountCondition();
		}
	},
	GOLD {
		// 这个类用来判断真实的元宝，而不是元宝加内币，是用来给交易系统使用的
		@SuppressWarnings("unchecked")
		@Override
		protected GoldCondition create() {
			return new GoldCondition();
		}
	},
	COUNTRY_IS_WEAK {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryIsWeakCondition create() {
			return new CountryIsWeakCondition();
		}
	},
	CREATIME_PLAYER {
		@SuppressWarnings("unchecked")
		protected CreateTimePlayerCondition create() {
			return new CreateTimePlayerCondition();
		}

	},
	YEAR_OF_DAY_REMAIN {
		@SuppressWarnings("unchecked")
		@Override
		protected YearOfDayRemainCondition create() {
			return new YearOfDayRemainCondition();
		}
	},
	COMBATSPIRIT_QUALITY {

		@SuppressWarnings("unchecked")
		@Override
		protected CombatSpiritQualityCondition create() {
			return new CombatSpiritQualityCondition();
		}

	},
	COMBATSPIRIT_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected CombatSpiritLevelCondition create() {
			return new CombatSpiritLevelCondition();
		}
	},
	COMBATSPIRIT_COMPARATOR {
		@SuppressWarnings("unchecked")
		@Override
		protected CombatSpiritComparaCondition create() {
			return new CombatSpiritComparaCondition();
		}
	},
	RANK_POSITION {
		@SuppressWarnings("unchecked")
		@Override
		protected RankPositionCondition create() {
			return new RankPositionCondition();
		}
	},
	INVESTIGATE_COLOR_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected InvestigateColorTotalCountCondition create() {
			return new InvestigateColorTotalCountCondition();
		}

	},
	INVESTIGATE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected InvestigateTotalCountCondition create() {
			return new InvestigateTotalCountCondition();
		}

	},
	YESTERDAY_HERO_RANK {

		@SuppressWarnings("unchecked")
		@Override
		protected YesterdayHeroRankCondition create() {
			return new YesterdayHeroRankCondition();
		}

	},
	GM {
		@SuppressWarnings("unchecked")
		@Override
		protected GMCondition create() {
			return new GMCondition();
		}
	},
	RECHARGE_ONCE {
		@SuppressWarnings("unchecked")
		@Override
		protected RechargeOnceCondition create() {
			return new RechargeOnceCondition();
		}
	},
	TOTAL_RECHARGE {
		@SuppressWarnings("unchecked")
		@Override
		protected TotalRechargeCondition create() {
			return new TotalRechargeCondition();
		}
	},
	BRICK_COLOR_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected BrickColorTotalCountCondition create() {
			return new BrickColorTotalCountCondition();
		}
	},
	BRICK_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected BrickTotalCountCondition create() {
			return new BrickTotalCountCondition();
		}
	},
	GAME_START {
		@SuppressWarnings("unchecked")
		@Override
		protected ServerOpenCondition create() {
			return new ServerOpenCondition();
		}
	},
	SERVER_LIMIT {
		@SuppressWarnings("unchecked")
		@Override
		protected ServerLimitCondition create() {
			return new ServerLimitCondition();
		}
	},
	OPENACTIVE_CONSUME {
		@SuppressWarnings("unchecked")
		@Override
		protected OpenActiveConsumeCondition create() {
			return new OpenActiveConsumeCondition();
		}
	},
	INVEST_TYPE_BUY_TIME {

		@SuppressWarnings("unchecked")
		@Override
		protected InvestTypeBuyTimeCondition create() {
			return new InvestTypeBuyTimeCondition();
		}
	},
	INVEST_AGATE_TYPE_BUY_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected InvestAgateTypeBuyTimeCondition create() {
			return new InvestAgateTypeBuyTimeCondition();
		}
	},
	OPENACTIVE_HORSE {
		@SuppressWarnings("unchecked")
		@Override
		protected OpenActiveHorseCondition create() {
			return new OpenActiveHorseCondition();
		}
	},
	OPENACTIVE_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected OpenActiveEnhanceCondition create() {
			return new OpenActiveEnhanceCondition();
		}
	},
	SERVER_OPEN_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected ServerOpenTimeCondition create() {
			return new ServerOpenTimeCondition();
		}
	},
	COUNTRY_POWER_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryPowerLevelCondition create() {
			return new CountryPowerLevelCondition();
		}
	},
	WORLD_MILITARY {
		@SuppressWarnings("unchecked")
		@Override
		protected WorldMilitaryCondition create() {
			return new WorldMilitaryCondition();
		}
	},
	WORLD_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected WorldLevelCondition create() {
			return new WorldLevelCondition();
		}
	},
	ACTIVEVALUE_REWARDCOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected ActiveValueRewardCountCondition create() {
			return new ActiveValueRewardCountCondition();
		}

	},
	ONLINE_REWARD_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected OnlineRewardCountCondition create() {
			return new OnlineRewardCountCondition();
		}
	},
	CIVILSALRY_RECEIVE {
		@SuppressWarnings("unchecked")
		@Override
		protected CivilSalaryReceiveCondition create() {
			return new CivilSalaryReceiveCondition();
		}
	},
	PLAYER_IS_RESERVEKING {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerIsReserveKing create() {
			return new PlayerIsReserveKing();
		}
	},
	KILL_ENEMY {
		@SuppressWarnings("unchecked")
		@Override
		protected KillEnemyCondition create() {
			return new KillEnemyCondition();
		}
	},
	KILL_KING {
		@SuppressWarnings("unchecked")
		@Override
		protected KillKingCondition create() {
			return new KillKingCondition();
		}
	},
	HUNT_BOSS_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected HuntBossCountCondition create() {
			return new HuntBossCountCondition();
		}
	},
	MONSTER_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected KillMonsterCountCondition create() {
			return new KillMonsterCountCondition();
		}
	},
	SERVER_CHARGE_OPEN_TIME {
		@SuppressWarnings("unchecked")
		@Override
		protected ServerChargeOpenTimeCondition create() {
			return new ServerChargeOpenTimeCondition();
		}
	},
	OPENACTIVE_COMPETE {
		@SuppressWarnings("unchecked")
		@Override
		protected OpenActiveCompeteCondition create() {
			return new OpenActiveCompeteCondition();
		}
	},
	MERGE_CONSUME {
		@SuppressWarnings("unchecked")
		@Override
		protected MergeConsumeGoldCondition create() {
			return new MergeConsumeGoldCondition();
		}
	},
	INVEST_REWARD_DRAW {

		@SuppressWarnings("unchecked")
		@Override
		protected InvestDrawRewardCondition create() {
			return new InvestDrawRewardCondition();
		}
	},
	INVEST_AGATE_REWARD_DRAW {
		@SuppressWarnings("unchecked")
		@Override
		protected InvestAgateDrawRewardCondition create() {
			return new InvestAgateDrawRewardCondition();
		}
	},
	COUNTRY_QUEST_SELECT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryQuestSelectCondition create() {
			return new CountryQuestSelectCondition();
		}
	},
	TOTAL_COUNTRYCOPY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected TotalCountryCopyCountCondition create() {
			return new TotalCountryCopyCountCondition();
		}
	},

	TREASURE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected TreasureCountCondition create() {
			return new TreasureCountCondition();
		}
	},

	COUNTRY_MISSION_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryMissionFinishCountCondition create() {
			return new CountryMissionFinishCountCondition();
		}
	},

	PACK_ITEM_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected PackItemCountCondition create() {
			return new PackItemCountCondition();
		}

	},

	ENHANCE_EXTRAPOWER {

		@SuppressWarnings("unchecked")
		@Override
		protected EnhanceExtraPowerCondition create() {
			return new EnhanceExtraPowerCondition();
		}

	},

	GROUPPURCHASE_ATTEND_AMOUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchasePlayerAmountCondition create() {
			return new GroupPurchasePlayerAmountCondition();
		}

	},
	GROUPPURCHASE_TWO_ATTEND_AMOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchaseTwoPlayerAmountCondition create() {
			return new GroupPurchaseTwoPlayerAmountCondition();
		}

	},

	GROUPPURCHASE_THREE_ATTEND_AMOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchaseThreePlayerAmountCondition create() {
			return new GroupPurchaseThreePlayerAmountCondition();
		}

	},

	GROUPPURCHASE_GOLD_AMOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchaseGoldAmountCondtion create() {
			return new GroupPurchaseGoldAmountCondtion();
		}

	},

	GROUPPURCHASE_TWO_GOLD_AMOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchaseTwoGoldAmountCondtion create() {
			return new GroupPurchaseTwoGoldAmountCondtion();
		}

	},
	GROUPPURCHASE_THREE_GOLD_AMOUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected GroupPurchaseThreeGoldAmountCondtion create() {
			return new GroupPurchaseThreeGoldAmountCondtion();
		}

	},
	SERVER_OPEN_TIME_COMPARE {

		@SuppressWarnings("unchecked")
		@Override
		protected ServerOpenTimeCompareCondition create() {
			return new ServerOpenTimeCompareCondition();
		}

	},

	TECHNOLOGY_GRADE {

		@SuppressWarnings("unchecked")
		@Override
		public CountryTechnologyGradeCondition create() {
			return new CountryTechnologyGradeCondition();
		}
	},

	TECHNOLOGY_BUILVALUE_HL_RATE {
		@SuppressWarnings("unchecked")
		@Override
		protected CountryTechnologyBuildValueHLRateCondtion create() {
			return new CountryTechnologyBuildValueHLRateCondtion();
		}

	},

	PLAYER_NOT_IN_TARGET_MAP {
		@SuppressWarnings("unchecked")
		@Override
		protected PlayerNotInTargetMapCondition create() {
			return new PlayerNotInTargetMapCondition();
		}
	},

	HORSE_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected HorseEnhanceCondition create() {
			return new HorseEnhanceCondition();
		}
	},
	SOUL_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected SoulEnhanceCondition create() {
			return new SoulEnhanceCondition();
		}
	},
	ARTIFACT_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected ArtifactEnhanceCondition create() {
			return new ArtifactEnhanceCondition();
		}
	},
	CELEBRATE_ACTIVITY_OPEN {

		@SuppressWarnings("unchecked")
		@Override
		protected CelebrateActivityOpenCondition create() {
			return new CelebrateActivityOpenCondition();
		}

	},

	FASHION_COUNT_NOT_ZERO {

		@SuppressWarnings("unchecked")
		@Override
		protected FashionCountNotZeroCondition create() {
			return new FashionCountNotZeroCondition();
		}

	},

	MERGE_HAS_DONE {
		@SuppressWarnings("unchecked")
		@Override
		protected MergeHasDoneCondition create() {
			return new MergeHasDoneCondition();
		}
	},

	MERGE_DAY_JUDGE {

		@SuppressWarnings("unchecked")
		@Override
		protected MergeDayJudgeCondition create() {
			return new MergeDayJudgeCondition();
		}
	},
	COMMON_CONSUME {

		@SuppressWarnings("unchecked")
		@Override
		protected CommonConsumeCondition create() {
			return new CommonConsumeCondition();
		}
	},
	LUCK_VALUE {
		@SuppressWarnings("unchecked")
		@Override
		protected LuckValueCondition create() {
			return new LuckValueCondition();
		}
	},
	FIRST_PAY_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected FirstPayCountCondition create() {
			return new FirstPayCountCondition();
		}
	},
	IDENTIFY_END_CONDITION {
		@SuppressWarnings("unchecked")
		@Override
		protected IdentifyEndCondition create() {
			return new IdentifyEndCondition();
		}
	},
	EVERYDAY_RECHARGE_NOT_REWARD {

		@SuppressWarnings("unchecked")
		@Override
		protected EveryDayRechargeRewardCondition create() {
			return new EveryDayRechargeRewardCondition();
		}
	},
	BATTLE_SCORE {
		@SuppressWarnings("unchecked")
		@Override
		protected BattleScoreCondition create() {
			return new BattleScoreCondition();
		}
	},
	CAN_ENHANCE {
		@SuppressWarnings("unchecked")
		@Override
		protected CanEnhanceEquipCondition create() {
			return new CanEnhanceEquipCondition();
		}
	},
	COMMON_TREASURE_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		protected CommonTreasureActiveCountCondition create() {
			return new CommonTreasureActiveCountCondition();
		}
	},
	ACC_LOGIN_DAYS {
		@SuppressWarnings("unchecked")
		@Override
		protected AccLoginDayCondition create() {
			return new AccLoginDayCondition();
		}

	},
	BEAUTY_ACTIVE {

		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlActiveCondition create() {
			return new BeautyGirlActiveCondition();
		}

	},
	BEAUTY_SKILL_NOT_LEARN {

		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlSkillNotLearnConidtion create() {
			return new BeautyGirlSkillNotLearnConidtion();
		}

	},

	BEAUTY_LEVEL {

		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlLevelCondition create() {
			return new BeautyGirlLevelCondition();
		}

	},
	BEAUTY_LEVEL_GREATER {

		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlLevelGreaterCondition create() {
			return new BeautyGirlLevelGreaterCondition();
		}

	},
	SIGN_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected SignCountCondition create() {
			return new SignCountCondition();
		}

	},
	SERVER_NAME_PREFIX {

		@SuppressWarnings("unchecked")
		@Override
		protected ServerNameCondition create() {
			return new ServerNameCondition();
		}

	},
	SERVER_OPEN_BETWEEN {

		@SuppressWarnings("unchecked")
		@Override
		protected ServerOpenBetweenCondition create() {
			return new ServerOpenBetweenCondition();
		}

	},

	WARBOOK_ITEM_COUNT_LESS {
		@SuppressWarnings("unchecked")
		@Override
		protected WarBookItemCountLessCondition create() {
			return new WarBookItemCountLessCondition();
		}
	},
	
	SEAL_ITEM_COUNT_LESS {
		@SuppressWarnings("unchecked")
		@Override
		protected SealItemCountLessCondition create() {
			return new SealItemCountLessCondition();
		}
	},


	HORSE_ITEM_COUNT_LESS {

		@SuppressWarnings("unchecked")
		@Override
		protected HorseItemCountLessCondition create() {
			return new HorseItemCountLessCondition();
		}

	},
	ARTIFACT_ITEM_COUNT_LESS {

		@SuppressWarnings("unchecked")
		@Override
		protected ArtifactItemCountLessCondition create() {
			return new ArtifactItemCountLessCondition();
		}

	},
	SOUL_ITEM_COUNT_LESS {

		@SuppressWarnings("unchecked")
		@Override
		protected SoulItemCountLessCondition create() {
			return new SoulItemCountLessCondition();
		}

	},
	BEAUTY_ITEM_COUNT_LESS {

		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlItemCountLessCondition create() {
			return new BeautyGirlItemCountLessCondition();
		}

	},

	BEAUTY_SUM_LEVEL {
		@SuppressWarnings("unchecked")
		@Override
		protected BeautyGirlSumLevelCondition create() {
			return new BeautyGirlSumLevelCondition();
		}

	},

	WARBOOK_GRADE_GREATER {

		@SuppressWarnings("unchecked")
		@Override
		protected WarbookGradeGreaterCondition create() {
			return new WarbookGradeGreaterCondition();
		}
	},
	SEAL_GRADE_GREATER {
		@SuppressWarnings("unchecked")
		@Override
		protected SealGradeGreaterCondition create() {
			return new SealGradeGreaterCondition();
		}
	},
	
	LUCKY_DRAW_COUNT {

		@SuppressWarnings("unchecked")
		@Override
		protected LuckyDrawCountCondition create() {
			return new LuckyDrawCountCondition();
		}
	},
	GOLD_TREASURY_CONDITION {
		@SuppressWarnings("unchecked")
		@Override
		protected GoldTreasuryCountCondition create() {
			return new GoldTreasuryCountCondition();
		}
	},
	CONSUME_GIFT_CONDITION {
		@SuppressWarnings("unchecked")
		@Override
		protected ConsumeGiftCondition create() {
			return new ConsumeGiftCondition();
		}
	},

	LIFEGRID_STORAGE_SIZE_GREATER {

		@SuppressWarnings("unchecked")
		@Override
		protected LifeGridStorageSizeGreaterCondition create() {
			return new LifeGridStorageSizeGreaterCondition();
		}

	},
	SUICIDE_ELEMENT_FULL {

		@SuppressWarnings("unchecked")
		@Override
		protected SuicideElementFullCondition create() {
			return new SuicideElementFullCondition();
		}

	},

	SUICIDE_TURN_GREATE_EQUAL {

		@SuppressWarnings("unchecked")
		@Override
		protected SuicideTurnGreaterEqualCondition create() {
			return new SuicideTurnGreaterEqualCondition();
		}

	},

	SUICIDE_LEVEL_GREATER_EQUAL {

		@SuppressWarnings("unchecked")
		@Override
		protected SuicidePlayerLevelCondition create() {
			return new SuicidePlayerLevelCondition();
		}

	},
/*	SEAL_ITEM_COUNT_LESS{

		@Override
		protected SuicidePlayerLevelCondition create() {
			return new SuicidePlayerLevelCondition();
		}
		
	},
	SEAL_GRADE_GREATER{

		@Override
		protected SuicidePlayerLevelCondition create() {
			return new SuicidePlayerLevelCondition();
		}
		
	}*/
	;

	protected abstract <T extends AbstractCoreCondition> T create();

	/**
	 * 构建货币判断条件
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static CurrencyCondition createCurrencyCondition(CurrencyType type, int value) {
		CurrencyCondition currencyCondition = CURRENCY.create();
		currencyCondition.setType(type);
		currencyCondition.value = value;
		return currencyCondition;
	}

	public static GoldCondition createGoldCondition(int value) {
		GoldCondition goldCondition = GOLD.create();
		goldCondition.value = value;
		return goldCondition;
	}

	public static CountryCurrencyCondition createCountryCurrencyCondition(CoppersType type, int value) {
		CountryCurrencyCondition currencyCondition = COUNTRY_CURRENCY.create();
		currencyCondition.setType(type);
		currencyCondition.value = value;
		return currencyCondition;
	}

	/**
	 * 背包剩余空间判断
	 * 
	 * @param value
	 * @return
	 */
	public static PackCondition createPackCondition(int value) {
		PackCondition packCondition = PACK.create();
		packCondition.value = value;
		return packCondition;
	}

	/**
	 * 技能施法者蓝量判断
	 * 
	 * @param value
	 * @return
	 */
	public static SkillMpCondition createSkillMpCondition(int value) {
		SkillMpCondition skillMpCondition = SKILL_MP.create();
		skillMpCondition.value = value;
		return skillMpCondition;
	}

	/**
	 * 技能施法者怒气判断
	 * 
	 * @param value
	 * @return
	 */
	public static SkillDpCondition createSkillDpCondition(int value) {
		SkillDpCondition skillDpCondition = SKILL_DP.create();
		skillDpCondition.value = value;
		return skillDpCondition;
	}

	/**
	 * 道具充足判断
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static ItemCondition createItemCondition(String key, int value) {
		ItemCondition itemCondition = ITEM.create();
		itemCondition.code = key;
		itemCondition.value = value;
		return itemCondition;
	}

	public static CountryCondition createCountryCondition(int countryValue) {
		CountryCondition countryCondition = COUNTRY_COND.create();
		countryCondition.value = countryValue;
		return countryCondition;
	}

	public static BetweenCronTimeCondition createBetweenCronTimeCondition(String start, String end) {
		BetweenCronTimeCondition bctc = new BetweenCronTimeCondition();
		bctc.setStartDate(start);
		bctc.setEndDate(end);
		return bctc;
	}

	public static ServerLimitCondition createServerLimitCondition(String limitKey) {
		ServerLimitCondition slc = SERVER_LIMIT.create();
		slc.code = limitKey;
		return slc;
	}

	/**
	 * 由于不同的condition需要的参数不同，所以不建议使用这个类型的condition构建方法。添加子类的构造方法请自行实现独立的构造方法
	 * 
	 * @param type
	 * @param code
	 * @param value
	 * @return
	 */
	@Deprecated
	public static <T extends AbstractCoreCondition> T createCondition(CoreConditionType type, String code, int value) {
		T condition = type.create();
		condition.code = code;
		condition.value = value;
		condition.init();
		return condition;
	}
}
