package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.List;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class OverlyCountStatBuffEffect extends StatBuffEffect {

	private int maxOverlyCount;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		maxOverlyCount = resource.getMaxOverlyCount();
	}

	@Override
	public void replace(Effect oldEffect, Effect newEffect) {
		if (oldEffect.getSkillId() == newEffect.getSkillId()) {
			if (oldEffect.getOverlyCount() >= maxOverlyCount) {
				newEffect.setOverlyCount(maxOverlyCount);
			} else {
				newEffect.setOverlyCount(oldEffect.getOverlyCount() + 1);
			}
		}
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		List<Stat> newStat = New.arrayList();
		for (Stat stat : stats[effect.getSkillLevel() - 1]) {
			Stat newProperty = stat.getNewProperty();
			newProperty.multipMerge(effect.getOverlyCount());
			newStat.add(newProperty);
		}
		effected.getGameStats().addModifiers(
				StatEffectId.valueOf(this.getEffectTemplateId() + "", StatEffectType.SKILL_EFFECT, !effect
						.getSkillTemplate().isCombatShow()), newStat);
	}

}