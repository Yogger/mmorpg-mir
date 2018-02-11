package com.mmorpg.mir.model.skill.effecttemplate;

public enum EffectTemplateType {
	/** 百分比伤害 */
	PERCENT_DAMAGEEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new PercentDamageEffectTemplate();
		}
	},
	/** 触发效果 */
	PROVOKER_EFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ProvokerEffect();
		}
	},
	/** 按照优先级触发效果 */
	PRIORITY_PROVOKER_EFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new PriorityProvokerEffect();
		}
	},
	/** 伤害 */
	DAMAGEEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new DamageEffectTemplate();
		}
	},
	/** 治疗 */
	HEALEFEECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new HealEffectTemplate();
		}
	},
	/** 恢复怒气效果 */
	HEAL_DP_EFEECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new HealDpEffectTemplate();
		}
	},
	/** 护盾 */
	SHIELD {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ShieldEffect();
		}
	},
	/** 百分比护盾 */
	SHIELD_PERCENT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ShieldPercentEffect();
		}
	},
	/** 次数护盾 */
	SHIELD_COUNT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ShieldCountEffect();
		}
	},
	/** HOT治疗 */
	HEAL_HOT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new HealHotEffect();
		}
	},
	/** 无法选取 */
	INSELECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new InselectEffect();
		}
	},
	/** 无敌斩 */
	INVINCIBLECUT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new InvincibleCutDamageEffectTemplate();
		}
	},
	/** 血包HOT治疗 */
	HPSTORE_HEAL_HOT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new HpstoreHealHotEffect();
		}
	},
	/** 蓝包HOT */
	MPSTORE_HEAL_HOT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new MpstoreHealHotEffect();
		}
	},
	/** 流血 */
	BLEEDEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BleedEffect();
		}
	},
	/** 军旗效果 */
	ARMYFLAG_EFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ArmyFlagEffect();
		}
	},
	/** 光环 */
	AURAEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new AuraEffect();
		}
	},
	/** 弱国（国旗/大臣）加层光环 */
	COUNTRY_AURAEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new CountryAuraEffect();
		}
	},
	/** 国家科技军旗光环效果 */
	COUNTRY_ARMYFLAG {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new CountryArmyFlagAuraEffect();
		}
	},
	/** 弱国（国旗/大臣）加层效果 */
	COUNTRY_STATBUFFEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new CountryStatBuffEffect();
		}
	},
	/** 砍国旗的国旗参与奖 光环 */
	COUNTRYFLAG_ACTEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new CountryFlagActEffect();
		}
	},
	/** 百分比流血 */
	PERCENT_BLEEDEFFECT {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BleedPercentEffect();
		}
	},
	/** 回城 */
	BACK_HOME {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BackHomeEffectTemplate();
		}
	},
	/** 减速 */
	SLOW {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new SlowEffect();
		}
	},
	/** 无敌 */
	GOD {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new GodEffect();
		}
	},
	/** 霸体 */
	PABODY {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new PaBodyEffect();
		}
	},
	/** 操练 */
	EXERCISE {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ExerciseEffect();
		}
	},
	/** 沉默 */
	SLIENCE {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new SlienceEffect();
		}
	},
	/** 眩晕 */
	STUN {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new StunEffect();
		}
	},
	/** 定身 */
	CANNOT_MOVE {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new CannotMoveEffect();
		}
	},
	/** 冲撞 */
	DASH {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new DashEffectTemplate();
		}
	},
	/** 瞬移 */
	WARP {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new WarpEffectTemplate();
		}
	},
	/** 属性增强 */
	STAT_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new StatBuffEffect();
		}
	},
	/** 属性增强,攻击移除 */
	STAT_BUFF_ATTACK_REMOVE {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new StatBuffAttackRemoveEffect();
		}
	},
	/** 时间可叠加的属性增强 */
	OVERLY_STAT_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new OverlyTimeStatBuffEffect();
		}
	},
	/** 效果可叠加的属性增强 */
	OVERLY_COUNT_STAT_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new OverlyCountStatBuffEffect();
		}
	},
	/** 内奸标记 */
	TRAITOR_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new TraitorEffect();
		}
	},
	/** 免死 */
	UNDEAD {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new UndeadEffect();
		}
	},
	/** 禁言标记 */
	FORBID_CHAT_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new ForbidChatEffect();
		}
	},
	/** 炸弹人标记 */
	BOMB_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BombEffect();
		}
	},
	/** 家族战经验BUF */
	GANGOFWAR_EXP_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new GangOfWarExpEffect();
		}
	},
	/** 免费复活 */
	FREE_BUFF {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new FreeReliveEffect();
		}
	},

	/** 板砖 */
	BRICK {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BrickEffect();
		}
	},

	/** 灰名 */
	GRAY {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new GrayEffect();
		}
	},
	/** 临时国王 */
	TEMP_KING {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new TempKingEffect();
		}
	},

	/** 召唤 */
	SUMMON {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new SummonEffect();
		}
	},
	/** 召唤大B哥 */
	BIG_BROTHER {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new BigBrotherEffect();
		}
	},
	/** 伤害光环 */
	DAMAGEHALO {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new DamageHaloEffect();
		}
	},
	/** 伤害吸收(死骑大) */
	DAMAGE_SUCK {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends EffectTemplate> T create() {
			return (T) new DamageSuckEffect();
		}
	},
	HERORANK {
		@SuppressWarnings("unchecked")
		@Override
		public HeroRankEffect create() {
			return new HeroRankEffect();
		}
	};

	public abstract <T extends EffectTemplate> T create();
}
