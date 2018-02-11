package com.mmorpg.mir.model.skill.effecttemplate;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.DamageType;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.windforce.common.utility.RandomUtils;

public class DamageEffectTemplate extends EffectTemplate {

	private int[] values;

	private int[] percents;

	/** 多倍暴击 */
	private int[] multiCriticals;

	/** 施法者自身攻击(PHYSICAL_ATTACK或者MAGICAL_ATTACK属性)百分比(0-10000)的真实伤害 */
	private int attackTrueDamagePercent;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
		multiCriticals = resource.getMultiCriticals();
		attackTrueDamagePercent = resource.getAttackTrueDamagePercent();
	}

	// 基础伤害=（（攻击*（1+加（减）攻百分比）+加攻绝对值）-（防御*（1+加（减）防百分比）+加防绝对值））*（技能加成+其它伤害加（减）成）+(技能无视防御伤害+其它无视防御伤害）
	// + 技能根据施法者的攻击产生的真实伤害+

	@Override
	public void calculate(Effect effect) {
		if (effect.getEffected().getEffectController().isAbnoramlSet(EffectId.GOD)) {
			effect.setReserved1(0);
		} else {

			long damage = MathUtil.calculateDamage(effect.getSkillLevel(), effect.getSkillDamageType(), effect,
					ArrayUtils.isEmpty(values) ? 0 : values[effect.getSkillLevel() - 1],
					percents[effect.getSkillLevel() - 1]);

			StatEnum attackType = ((effect.getSkillDamageType() == com.mmorpg.mir.model.skill.resource.DamageType.MAGICAL) ? StatEnum.MAGICAL_ATTACK
					: StatEnum.PHYSICAL_ATTACK);
			long attack = effect.getEffector().getGameStats().getCurrentStat(attackType);
			long randomResult = (long) (Math.max(damage, .05 * attack) * (RandomUtils.nextDouble() * .2 + .9));

			long rate = MathUtil.calDamageRate(effect);
			long typeResult = selectDamageType(DamageType.valueOf(effect.getDamageType())).calcDamage(randomResult,
					rate);

			// 愤勇类技能，增加的多倍暴击
			if (multiCriticals != null && multiCriticals.length == 2) {
				int multiCritical = RandomUtils.betweenInt(multiCriticals[0], multiCriticals[1], true);
				typeResult *= multiCritical;
			}

			// 贪狼类技能
			if (attackTrueDamagePercent != 0) {
				typeResult += (int) (attack * (attackTrueDamagePercent * 1.0 / 10000));
			}

			// 盾
			Effect shield = effect.getEffected().getEffectController().getAnormalEffect(ShieldEffect.SHIELD_STACK);
			if (shield != null) {
				if (shield.getReserved3() > typeResult) {
					shield.reduceReserved3(damage);
					typeResult = 0;
				} else {
					typeResult -= shield.getReserved3();
					shield.endEffect();
				}
			}

			// 盾
			Effect shieldCount = effect.getEffected().getEffectController()
					.getAnormalEffect(ShieldCountEffect.SHIELD_STACK);
			if (shieldCount != null) {
				shieldCount.reduceReserved3(1);
				typeResult = 0;
				if (shieldCount.getReserved3() <= 0) {
					shieldCount.endEffect();
				}
			}

			effect.setReserved1(typeResult);
			// 计算吸血和反伤
			effect.calculateSepecialEffect(effect.getReserved1());
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

	public int getAttackTrueDamagePercent() {
		return attackTrueDamagePercent;
	}

	public void setAttackTrueDamagePercent(int attackTrueDamagePercent) {
		this.attackTrueDamagePercent = attackTrueDamagePercent;
	}

}
