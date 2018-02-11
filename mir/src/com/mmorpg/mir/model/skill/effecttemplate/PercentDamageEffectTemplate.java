package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class PercentDamageEffectTemplate extends EffectTemplate {

	private int[] values;

	private int[] percents;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
	}

	// 基础伤害=（（攻击*（1+加（减）攻百分比）+加攻绝对值）-（防御*（1+加（减）防百分比）+加防绝对值））*（技能加成+其它伤害加（减）成）+(技能无视防御伤害+其它无视防御伤害）

	@Override
	public void calculate(Effect effect) {
		if (effect.getEffected().getEffectController().isAbnoramlSet(EffectId.GOD)) {
			effect.setReserved1(0);
		} else {
			long damage = effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXHP);
			int v = 0;
			if (values != null && values.length >= effect.getSkillLevel()) {
				v = values[effect.getSkillLevel() - 1];
			}
			damage = (long) (Math.ceil((damage * 1.0 / 10000) * percents[effect.getSkillLevel() - 1]) + v);
			// 盾
			Effect shield = effect.getEffected().getEffectController().getAnormalEffect(ShieldEffect.SHIELD_STACK);
			if (shield != null) {
				if (shield.getReserved3() > damage) {
					shield.reduceReserved3(damage);
					damage = 0;
				} else {
					damage -= shield.getReserved3();
					shield.endEffect();
				}
			}

			// 盾
			Effect shieldCount = effect.getEffected().getEffectController()
					.getAnormalEffect(ShieldCountEffect.SHIELD_STACK);
			if (shieldCount != null) {
				shieldCount.reduceReserved3(1);
				damage = 0;
				if (shieldCount.getReserved3() <= 0) {
					shieldCount.endEffect();
				}
			}

			effect.setReserved1(damage);
		}
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		if (!effect.canAttackType(effect.getEffected())) {
			return;
		}
		// 这里用来真正的扣减伤害
		DamageResult damageResult = DamageResult.valueOf();
		effect.getEffected().getController()
				.onAttack(effect.getEffector(), effect.getSkillId(), effect.getReserved1(), damageResult);
		if (damageResult.getDamage() != 0) {
			effect.setReserved1(damageResult.getDamage());
		}
		effect.getEffector().getObserveController().notifyAttackObservers(effect.getEffected());
		// 计算吸血和反伤
		effect.calculateSepecialEffect(effect.getReserved1());
		if (effect.getBloodSuckValue() > 0) {
			effect.getEffector().getLifeStats().increaseHp(effect.getBloodSuckValue());
		}
		if (effect.getReboundValue() > 0) {
			effect.getEffector().getController().onAttack(effect.getEffected(), effect.getReboundValue(), damageResult);
		}
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public int[] getPercents() {
		return percents;
	}

	public void setPercents(int[] percents) {
		this.percents = percents;
	}

}
