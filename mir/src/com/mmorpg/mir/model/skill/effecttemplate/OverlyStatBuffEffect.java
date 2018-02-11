package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class OverlyStatBuffEffect extends StatBuffEffect {

	private int maxOverlyTime;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		maxOverlyTime = resource.getMaxOverlyTime();
	}

	@Override
	public void replace(Effect oldEffect, Effect newEffect) {
		if (oldEffect.getSkillId() == newEffect.getSkillId()) {
			long overlyDuration = oldEffect.getElapsedTime() + newEffect.getDuration();
			if (overlyDuration > maxOverlyTime) {
				newEffect.setDuration(maxOverlyTime);
				newEffect.setEndTime(System.currentTimeMillis() + newEffect.getDuration());
			} else {
				newEffect.addDuration(oldEffect.getElapsedTime());
			}
		}
	}
}