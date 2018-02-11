package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;

public class GodEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.GOD.getEffectId(), true);
		effect.getEffected().getObserveController().attach(new ActionObserver(ObserverType.SKILLUSE) {

			@Override
			public void skilluse(Skill skill) {
				effect.endEffect();
			}

		});
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}