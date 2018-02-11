package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.List;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class StatBuffEffect extends EffectTemplate {
	protected int value;
	protected Stat[][] stats;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		this.stats = resource.getStats();
	}

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getGameStats().endModifiers(
				StatEffectId.valueOf(this.getEffectTemplateId() + "", StatEffectType.SKILL_EFFECT, !effect
						.getSkillTemplate().isCombatShow()));
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		List<Stat> newStat = New.arrayList();
		for (Stat stat : stats[effect.getSkillLevel() - 1]) {
			newStat.add(stat.getNewProperty());
		}
		effected.getGameStats().addModifiers(
				StatEffectId.valueOf(this.getEffectTemplateId() + "", StatEffectType.SKILL_EFFECT, !effect
						.getSkillTemplate().isCombatShow()), newStat);
	}
}