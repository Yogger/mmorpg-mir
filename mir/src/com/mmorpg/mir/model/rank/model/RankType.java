package com.mmorpg.mir.model.rank.model;

import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.model.rankelement.ActivityHeroRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.ArtifactLevelElement;
import com.mmorpg.mir.model.rank.model.rankelement.BattleScoreElement;
import com.mmorpg.mir.model.rank.model.rankelement.CelebrateConsumeElement;
import com.mmorpg.mir.model.rank.model.rankelement.ChristmasConsumeElement;
import com.mmorpg.mir.model.rank.model.rankelement.ConsumeRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.CountryRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.EnhanceRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.HeroRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.HorseGradeElement;
import com.mmorpg.mir.model.rank.model.rankelement.LadderRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.MedalElement;
import com.mmorpg.mir.model.rank.model.rankelement.MergeConsumeElement;
import com.mmorpg.mir.model.rank.model.rankelement.MilitaryRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.PlayerLevelRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.ProtectureElement;
import com.mmorpg.mir.model.rank.model.rankelement.RomaticConsumeRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.SealRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.SoulRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.ThousandConsumeRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.TreasureElement;
import com.mmorpg.mir.model.rank.model.rankelement.TreeConsumeElement;
import com.mmorpg.mir.model.rank.model.rankelement.WarbookRankElement;
import com.mmorpg.mir.model.rank.model.rankelement.ZhanGuoRankElement;

/**
 * 排行榜类型
 * 
 * @author 37wan
 */
public enum RankType {
	/** 名将试炼副本 */
	LADDER(1) {
		@Override
		public Class<?> getElementClassType() {
			return LadderRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public LadderRankElement createElement(Player player) {
			return LadderRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_LADDER;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_LADDER;
			}
			return RankType.ZHAO_LADDER;
		}
	},
	QI_LADDER(101) {
		@Override
		public Class<?> getElementClassType() {
			return LadderRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public LadderRankElement createElement(Player player) {
			return LadderRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LADDER;
		}
	},
	CHU_LADDER(201) {
		@Override
		public Class<?> getElementClassType() {
			return LadderRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public LadderRankElement createElement(Player player) {
			return LadderRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LADDER;
		}
	},
	ZHAO_LADDER(301) {
		@Override
		public Class<?> getElementClassType() {
			return LadderRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public LadderRankElement createElement(Player player) {
			return LadderRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LADDER;
		}
	},
	/** 玩家等级 */
	LEVEL(2) {
		@Override
		public Class<?> getElementClassType() {
			return PlayerLevelRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public PlayerLevelRankElement createElement(Player player) {
			return PlayerLevelRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_LEVEL;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_LEVEL;
			}
			return RankType.ZHAO_LEVEL;
		}
	},
	/** 齐国的玩家等级榜 */
	QI_LEVEL(11) {
		@Override
		public Class<?> getElementClassType() {
			return PlayerLevelRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public PlayerLevelRankElement createElement(Player player) {
			return PlayerLevelRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LEVEL;
		}

	},
	/** 楚国的玩家等级榜 */
	CHU_LEVEL(12) {
		@Override
		public Class<?> getElementClassType() {
			return PlayerLevelRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public PlayerLevelRankElement createElement(Player player) {
			return PlayerLevelRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LEVEL;
		}
	},
	/** 赵国的玩家等级榜 */
	ZHAO_LEVEL(13) {
		@Override
		public Class<?> getElementClassType() {
			return PlayerLevelRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public PlayerLevelRankElement createElement(Player player) {
			return PlayerLevelRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.LEVEL;
		}
	},
	/** 军衔 */
	MILITARY(3) {
		@Override
		public Class<?> getElementClassType() {
			return MilitaryRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MilitaryRankElement createElement(Player player) {
			return MilitaryRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_MILITARY;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_MILITARY;
			}
			return RankType.ZHAO_MILITARY;
		}
	},
	QI_MILITARY(103) {
		@Override
		public Class<?> getElementClassType() {
			return MilitaryRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MilitaryRankElement createElement(Player player) {
			return MilitaryRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MILITARY;
		}
	},
	CHU_MILITARY(203) {
		@Override
		public Class<?> getElementClassType() {
			return MilitaryRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MilitaryRankElement createElement(Player player) {
			return MilitaryRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MILITARY;
		}
	},
	ZHAO_MILITARY(303) {
		@Override
		public Class<?> getElementClassType() {
			return MilitaryRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MilitaryRankElement createElement(Player player) {
			return MilitaryRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MILITARY;
		}
	},
	/** 坐骑排行 */
	HORSE(4) {
		@Override
		public Class<?> getElementClassType() {
			return HorseGradeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HorseGradeElement createElement(Player player) {
			return HorseGradeElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_HORSE;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_HORSE;
			}
			return RankType.ZHAO_HORSE;
		}
	},
	QI_HORSE(104) {
		@Override
		public Class<?> getElementClassType() {
			return HorseGradeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HorseGradeElement createElement(Player player) {
			return HorseGradeElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.HORSE;
		}
	},
	CHU_HORSE(204) {
		@Override
		public Class<?> getElementClassType() {
			return HorseGradeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HorseGradeElement createElement(Player player) {
			return HorseGradeElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.HORSE;
		}
	},
	ZHAO_HORSE(304) {
		@Override
		public Class<?> getElementClassType() {
			return HorseGradeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HorseGradeElement createElement(Player player) {
			return HorseGradeElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.HORSE;
		}
	},
	/** 神兵排行 */
	ARTIFACT(5) {
		@Override
		public Class<?> getElementClassType() {
			return ArtifactLevelElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArtifactLevelElement createElement(Player player) {
			return ArtifactLevelElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_ARTIFACT;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_ARTIFACT;
			}
			return RankType.ZHAO_ARTIFACT;
		}
	},
	QI_ARTIFACT(105) {
		@Override
		public Class<?> getElementClassType() {
			return ArtifactLevelElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArtifactLevelElement createElement(Player player) {
			return ArtifactLevelElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ARTIFACT;
		}
	},
	CHU_ARTIFACT(205) {
		@Override
		public Class<?> getElementClassType() {
			return ArtifactLevelElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArtifactLevelElement createElement(Player player) {
			return ArtifactLevelElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ARTIFACT;
		}
	},
	ZHAO_ARTIFACT(305) {
		@Override
		public Class<?> getElementClassType() {
			return ArtifactLevelElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArtifactLevelElement createElement(Player player) {
			return ArtifactLevelElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ARTIFACT;
		}
	},
	/** 英魂排行 */
	SOUL(6) {
		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_SOUL;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_SOUL;
			}
			return RankType.ZHAO_SOUL;
		}
	},
	QI_SOUL(106) {
		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.SOUL;
		}
	},
	CHU_SOUL(206) {
		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.SOUL;
		}
	},
	ZHAO_SOUL(306) {
		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.SOUL;
		}
	},
	/** 齐国今日英雄榜 */
	QI_TODAY_HERO(7) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HeroRankElement createElement(Player player) {
			return HeroRankElement.valueOf(player);
		}
	},
	/** 楚国今日英雄榜 */
	CHU_TODAY_HERO(14) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HeroRankElement createElement(Player player) {
			return HeroRankElement.valueOf(player);
		}
	},
	/** 赵国今日英雄榜 */
	ZHAO_TODAY_HERO(15) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HeroRankElement createElement(Player player) {
			return HeroRankElement.valueOf(player);
		}
	},
	/** 齐国昨日英雄榜 */
	QI_YESTERDAY_HERO(8) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@Override
		public <T extends RankRow> T createElement(Player player) {
			// 不需要
			return null;
		}
	},
	/** 楚国昨日英雄榜 */
	CHU_YESTERDAY_HERO(16) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@Override
		public <T extends RankRow> T createElement(Player player) {
			// 不需要
			return null;
		}
	},
	/** 赵国昨日英雄榜 */
	ZHAO_YESTERDAY_HERO(17) {
		@Override
		public Class<?> getElementClassType() {
			return HeroRankElement.class;
		}

		@Override
		public <T extends RankRow> T createElement(Player player) {
			// 不需要
			return null;
		}
	},
	/** 综合战力 */
	COMPOSITE_BATTLESCORE(9) {
		@Override
		public Class<?> getElementClassType() {
			return BattleScoreElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public BattleScoreElement createElement(Player player) {
			return BattleScoreElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_POWER;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_POWER;
			}
			return RankType.ZHAO_POWER;
		}
	},
	/** 国家实力 */
	COUNTRY_POWER(10) {

		@Override
		public Class<?> getElementClassType() {
			return CountryRankElement.class;
		}

		@Override
		public <T extends RankRow> T createElement(Player player) {
			return null;
		}

	},
	/** 齐国国家实力 */
	QI_POWER(22) {
		@Override
		public Class<?> getElementClassType() {
			return BattleScoreElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public BattleScoreElement createElement(Player player) {
			return BattleScoreElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.COMPOSITE_BATTLESCORE;
		}
	},
	/** 楚国国家实力 */
	CHU_POWER(23) {
		@Override
		public Class<?> getElementClassType() {
			return BattleScoreElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public BattleScoreElement createElement(Player player) {
			return BattleScoreElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.COMPOSITE_BATTLESCORE;
		}
	},
	/** 赵国国家实力 */
	ZHAO_POWER(24) {
		@Override
		public Class<?> getElementClassType() {
			return BattleScoreElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public BattleScoreElement createElement(Player player) {
			return BattleScoreElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.COMPOSITE_BATTLESCORE;
		}
	},
	/** 勋章排行榜 */
	MEDAL(19) {

		@Override
		public Class<?> getElementClassType() {
			return MedalElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MedalElement createElement(Player player) {
			return MedalElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_MEDAL;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_MEDAL;
			}
			return RankType.ZHAO_MEDAL;
		}
	},
	QI_MEDAL(119) {
		@Override
		public Class<?> getElementClassType() {
			return MedalElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MedalElement createElement(Player player) {
			return MedalElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MEDAL;
		}
	},
	CHU_MEDAL(219) {
		@Override
		public Class<?> getElementClassType() {
			return MedalElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MedalElement createElement(Player player) {
			return MedalElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MEDAL;
		}
	},
	ZHAO_MEDAL(319) {
		@Override
		public Class<?> getElementClassType() {
			return MedalElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MedalElement createElement(Player player) {
			return MedalElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.MEDAL;
		}
	},
	/** 护符排行榜 */
	PROTECTURE(20) {

		@Override
		public Class<?> getElementClassType() {
			return ProtectureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ProtectureElement createElement(Player player) {
			return ProtectureElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_PROTECTURE;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_PROTECTURE;
			}
			return RankType.ZHAO_PROTECTURE;
		}
	},
	QI_PROTECTURE(120) {
		@Override
		public Class<?> getElementClassType() {
			return ProtectureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ProtectureElement createElement(Player player) {
			return ProtectureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.PROTECTURE;
		}
	},
	CHU_PROTECTURE(220) {
		@Override
		public Class<?> getElementClassType() {
			return ProtectureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ProtectureElement createElement(Player player) {
			return ProtectureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.PROTECTURE;
		}
	},
	ZHAO_PROTECTURE(320) {
		@Override
		public Class<?> getElementClassType() {
			return ProtectureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ProtectureElement createElement(Player player) {
			return ProtectureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.PROTECTURE;
		}
	},
	/** 宝物排行榜 */
	TREASURE(21) {

		@Override
		public Class<?> getElementClassType() {
			return TreasureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreasureElement createElement(Player player) {
			return TreasureElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_TREASURE;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_TREASURE;
			}
			return RankType.ZHAO_TREASURE;
		}
	},
	QI_TREASURE(121) {
		@Override
		public Class<?> getElementClassType() {
			return TreasureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreasureElement createElement(Player player) {
			return TreasureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.TREASURE;
		}
	},
	CHU_TREASURE(221) {
		@Override
		public Class<?> getElementClassType() {
			return TreasureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreasureElement createElement(Player player) {
			return TreasureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.TREASURE;
		}
	},
	ZHAO_TREASURE(321) {
		@Override
		public Class<?> getElementClassType() {
			return TreasureElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreasureElement createElement(Player player) {
			return TreasureElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.TREASURE;
		}
	},
	/** 消费活动排行榜 */
	ACTIVITY_CONSUME_TYPE(25) {

		@Override
		public Class<?> getElementClassType() {
			return ConsumeRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ConsumeRankElement createElement(Player player) {
			return ConsumeRankElement.valueOf(player);
		}

	},
	/** 等级活动排行榜 */
	ACTIVITY_LEVEL_TYPE(26) {

		@Override
		public Class<?> getElementClassType() {
			return PlayerLevelRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public PlayerLevelRankElement createElement(Player player) {
			return PlayerLevelRankElement.valueOf(player);
		}

	},
	/** 坐骑开服竞技 */
	ACTIVITY_HORSE(29) {
		@Override
		public Class<?> getElementClassType() {
			return HorseGradeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public HorseGradeElement createElement(Player player) {
			return HorseGradeElement.valueOf(player);
		}
	},
	/** 装备强化开服竞技 */
	ACTIVITY_ENHANCEEQUIP(30) {
		@Override
		public Class<?> getElementClassType() {
			return EnhanceRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public EnhanceRankElement createElement(Player player) {
			return EnhanceRankElement.valueOf(player);
		}
	},
	/** 神兵开服竞技 */
	ACTIVITY_ARTIFACT(31) {
		@Override
		public Class<?> getElementClassType() {
			return ArtifactLevelElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ArtifactLevelElement createElement(Player player) {
			return ArtifactLevelElement.valueOf(player);
		}
	},
	/** 军衔开服竞技 */
	ACTIVITY_MILITARY(32) {
		@Override
		public Class<?> getElementClassType() {
			return MilitaryRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MilitaryRankElement createElement(Player player) {
			return MilitaryRankElement.valueOf(player);
		}
	},
	/** 战斗力开服竞技 */
	ACTIVITY_FIGHTPOWER(33) {
		@Override
		public Class<?> getElementClassType() {
			return BattleScoreElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public BattleScoreElement createElement(Player player) {
			return BattleScoreElement.valueOf(player);
		}
	},
	/** 英魂 */
	ACTIVITY_SOUL(34) {

		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

	},

	/** 老服8日竞技英魂 */
	OLD_ACTIVITY_SOUL(35) {

		@Override
		public Class<?> getElementClassType() {
			return SoulRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SoulRankElement createElement(Player player) {
			return SoulRankElement.valueOf(player);
		}

	},
	/** 合服消费活动排行榜 */
	MERGE_ACTIVITY_CONSUME(36) {

		@Override
		public Class<?> getElementClassType() {
			return MergeConsumeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public MergeConsumeElement createElement(Player player) {
			return MergeConsumeElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}

	},

	CHRISTMAS_ACTIVITY_CONSUME(37) {

		@Override
		public Class<?> getElementClassType() {
			return ChristmasConsumeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ChristmasConsumeElement createElement(Player player) {
			return ChristmasConsumeElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}

	},
	/** 国家英雄排行榜 **/
	ACTIVITY_COUNTRY_HERO(38) {

		@Override
		public Class<?> getElementClassType() {
			return ActivityHeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActivityHeroRankElement createElement(Player player) {
			return ActivityHeroRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_ACTIVITY_COUNTRY_HERO;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_ACTIVITY_COUNTRY_HERO;
			}
			return RankType.ZHAO_ACTIVITY_COUNTRY_HERO;
		}
	},
	QI_ACTIVITY_COUNTRY_HERO(39) {

		@Override
		public Class<?> getElementClassType() {
			return ActivityHeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActivityHeroRankElement createElement(Player player) {
			return ActivityHeroRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ACTIVITY_COUNTRY_HERO;
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},
	CHU_ACTIVITY_COUNTRY_HERO(40) {

		@Override
		public Class<?> getElementClassType() {
			return ActivityHeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActivityHeroRankElement createElement(Player player) {
			return ActivityHeroRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ACTIVITY_COUNTRY_HERO;
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},
	ZHAO_ACTIVITY_COUNTRY_HERO(51) {

		@Override
		public Class<?> getElementClassType() {
			return ActivityHeroRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ActivityHeroRankElement createElement(Player player) {
			return ActivityHeroRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.ACTIVITY_COUNTRY_HERO;
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},
	CELEBRATE_CONSUME(52) {
		@Override
		public Class<?> getElementClassType() {
			return CelebrateConsumeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public CelebrateConsumeElement createElement(Player player) {
			return CelebrateConsumeElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},

	THOUSAND_CONSUME(53) {
		@Override
		public Class<?> getElementClassType() {
			return ThousandConsumeRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ThousandConsumeRankElement createElement(Player player) {
			return ThousandConsumeRankElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},

	TREE_CONSUME(54) {

		@Override
		public Class<?> getElementClassType() {
			return TreeConsumeElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreeConsumeElement createElement(Player player) {
			return TreeConsumeElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},

	ZHANGUO_CONSUME(55) {

		@Override
		public Class<?> getElementClassType() {
			return ZhanGuoRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ZhanGuoRankElement createElement(Player player) {
			return ZhanGuoRankElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},

	ROMATIC_CONSUME(56) {

		@Override
		public Class<?> getElementClassType() {
			return RomaticConsumeRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public RomaticConsumeRankElement createElement(Player player) {
			return RomaticConsumeRankElement.valueOf(player);
		}

		@Override
		public boolean isCompeteRank() {
			return true;
		}
	},

	WARBOOK(57) {

		@Override
		public Class<?> getElementClassType() {
			return WarbookRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public WarbookRankElement createElement(Player player) {
			return WarbookRankElement.valueOf(player);
		}

		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_WARBOOK;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_WARBOOK;
			}
			return RankType.ZHAO_WARBOOK;
		}

	},
	QI_WARBOOK(58) {

		@Override
		public Class<?> getElementClassType() {
			return WarbookRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public WarbookRankElement createElement(Player player) {
			return WarbookRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.WARBOOK;
		}
	},

	CHU_WARBOOK(59) {

		@Override
		public Class<?> getElementClassType() {
			return WarbookRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public WarbookRankElement createElement(Player player) {
			return WarbookRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.WARBOOK;
		}
	},
	ZHAO_WARBOOK(60) {
		@Override
		public Class<?> getElementClassType() {
			return WarbookRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public WarbookRankElement createElement(Player player) {
			return WarbookRankElement.valueOf(player);
		}

		@Override
		public RankType getRootRank() {
			return RankType.WARBOOK;
		}
	},
	SEAL(61) {

		@Override
		public Class<?> getElementClassType() {
			return SealRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SealRankElement createElement(Player player) {
			return SealRankElement.valueOf(player);
		}
		
		@Override
		public RankType getCountryRank(int countryValue) {
			if (countryValue == CountryId.C1.getValue()) {
				return RankType.QI_SEAL;
			} else if (countryValue == CountryId.C2.getValue()) {
				return RankType.CHU_SEAL;
			}
			return RankType.ZHAO_SEAL;
		}

	},
	
	QI_SEAL(62) {
		@Override
		public RankType getRootRank() {
			return RankType.SEAL;
		}

		@Override
		public Class<?> getElementClassType() {
			return SealRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SealRankElement createElement(Player player) {
			return SealRankElement.valueOf(player);
		}
	},
	
	CHU_SEAL(63) {
		@Override
		public RankType getRootRank() {
			return RankType.SEAL;
		}

		@Override
		public Class<?> getElementClassType() {
			return SealRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SealRankElement createElement(Player player) {
			return SealRankElement.valueOf(player);
		}
	},
	
	ZHAO_SEAL(64) {
		@Override
		public RankType getRootRank() {
			return RankType.SEAL;
		}

		@Override
		public Class<?> getElementClassType() {
			return SealRankElement.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public SealRankElement createElement(Player player) {
			return SealRankElement.valueOf(player);
		}
	},
	;
	private final int value;

	RankType(int v) {
		this.value = v;
	}

	public int getValue() {
		return this.value;
	}

	public static RankType valueOf(int c) {
		for (RankType t : values()) {
			if (t.value == c) {
				return t;
			}
		}
		return null;
	}

	public abstract Class<?> getElementClassType();

	public abstract <T extends RankRow> T createElement(Player player);

	public RankType getCountryRank(int countryValue) {
		return this;
	}

	public RankType getRootRank() {
		return this;
	}

	public boolean isCompeteRank() {
		return false;
	}

	public static void main(String[] args) {
		for (RankType type : RankType.values()) {
			System.out.println(type.name() + "  " + type.getValue());
		}
		System.out.println(RankType.LEVEL.getCountryRank(1) == RankType.QI_LEVEL);
		System.out.println(RankType.QI_LEVEL.getCountryRank(1) == RankType.QI_LEVEL);
	}
}
