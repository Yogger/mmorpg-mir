package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;

public class CannotMoveEffect extends EffectTemplate {

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
		effected.getEffectController().unsetAbnormal(EffectId.CANNOT_MOVE.getEffectId());
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		if (!effected.isObjectType(ObjectType.BOSS) && !effected.getEffectController().isAbnoramlSet(EffectId.PABODY)) {
			effected.getEffectController().setAbnormal(EffectId.CANNOT_MOVE.getEffectId());
		}
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}