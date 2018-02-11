package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;

public class SlienceEffect extends EffectTemplate {

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
		effected.getEffectController().unsetAbnormal(EffectId.SILENCE.getEffectId(), true);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		if (!effected.isObjectType(ObjectType.BOSS)) {
			effected.getEffectController().setAbnormal(EffectId.SILENCE.getEffectId(), true);
			if (effected.isCasting()) {
				effected.updateCasting(null);
			}
			effected.removeBackHome();
		}
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}