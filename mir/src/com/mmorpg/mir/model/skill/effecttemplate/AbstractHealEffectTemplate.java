package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public abstract class AbstractHealEffectTemplate extends EffectTemplate {

	protected int[] values;

	protected int[] percents;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
	}

	@Override
	public void applyEffect(Effect effect) {
	}

	@Override
	public void calculate(Effect effect) {
		effect.setReserved1(values[effect.getSkillLevel() - 1]);
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected long getCurrentStatValue(Effect effect) {
		return effect.getEffected().getLifeStats().getCurrentHp();
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	protected long getMaxStatValue(Effect effect) {
		return effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXHP);
	}

}
