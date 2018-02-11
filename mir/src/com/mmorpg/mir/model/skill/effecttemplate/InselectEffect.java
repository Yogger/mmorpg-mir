package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;

public class InselectEffect extends EffectTemplate {

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
		effected.getEffectController().unsetAbnormal(EffectId.INSELECT.getEffectId(), true);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.INSELECT.getEffectId(), true);
		effected.getObserveController().attach(new ActionObserver(ObserverType.ATTACK) {
			@Override
			public void attack(Creature creature) {
				effected.getEffectController().unsetAbnormal(EffectId.INSELECT.getEffectId(), true);
			}
		});
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}