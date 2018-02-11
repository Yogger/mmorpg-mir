package com.mmorpg.mir.model.skill.effecttemplate;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.DamageType;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public abstract class EffectTemplate {

	private int effectTemplateId;

	protected int duration;
	/** 是否可以暴击 */
	private DamageType[] damageTypes;

	public void init(EffectTemplateResource resource) {
		this.effectTemplateId = resource.getEffectTemplateId();
		this.duration = resource.getDuration();
		this.damageTypes = resource.getDamageTypes();
	}

	public DamageType selectDamageType(DamageType type) {
		if (ArrayUtils.isEmpty(damageTypes)) {
			return DamageType.NORMAL;
		}
		for (DamageType damageType : damageTypes) {
			if (damageType == type) {
				return damageType;
			}
		}
		return DamageType.NORMAL;
	}

	/**
	 * Calculate effect result
	 * 
	 * @param effect
	 */
	public abstract void calculate(Effect effect);

	/**
	 * Apply effect to effected
	 * 
	 * @param effect
	 */
	public abstract void applyEffect(Effect effect);

	/**
	 * Start effect on effected
	 * 
	 * @param effect
	 */
	public void startEffect(Effect effect) {
	};

	/**
	 * Do periodic effect on effected
	 * 
	 * @param effect
	 */
	public void onPeriodicAction(Effect effect) {
		if (effect.getIsStopped().get()) {
			effect.doEndEffect();
		}
	};

	/**
	 * Do replace
	 * 
	 * @param oldEffect
	 * @param newEffect
	 */
	public void replace(Effect oldEffect, Effect newEffect) {
	}

	/**
	 * End effect on effected
	 * 
	 * @param effect
	 */
	public void endEffect(Effect effect) {
	}

	public int getEffectTemplateId() {
		return effectTemplateId;
	}

	public void setEffectTemplateId(int effectTemplateId) {
		this.effectTemplateId = effectTemplateId;
	};

	public int getDuration() {
		return duration;
	}

	public DamageType[] getDamageTypes() {
		return damageTypes;
	}

	public void setDamageTypes(DamageType[] damageTypes) {
		this.damageTypes = damageTypes;
	}

}
