package com.mmorpg.mir.model.skill.effecttemplate;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;

public class HealDpEffectTemplate extends AbstractHealEffectTemplate {

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
	}

	@Override
	public void calculate(Effect effect) {
		long damage = MathUtil.calculateHeal(effect.getSkillLevel(), effect.getSkillDamageType(), effect, ArrayUtils
				.isEmpty(values) ? 0 : values[effect.getSkillLevel() - 1], ArrayUtils.isEmpty(percents) ? 0
				: percents[effect.getSkillLevel() - 1]);
		effect.setHealDp(damage);
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats() instanceof PlayerLifeStats) {
			PlayerLifeStats playerLifeStats = (PlayerLifeStats) effect.getEffected().getLifeStats();
			playerLifeStats.increaseDp(effect.getHealDp());
		}
	}
}