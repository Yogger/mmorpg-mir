package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;

public class UndeadEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
		// 只能对自己释放
		Creature effected = effect.getEffector();
		effected.getEffectController().unsetAbnormal(EffectId.UNDEAD.getEffectId(), true);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffector();
		effected.getEffectController().setAbnormal(EffectId.UNDEAD.getEffectId(), true);
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}