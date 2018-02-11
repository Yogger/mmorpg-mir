package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.skill.effect.Effect;

public class ArmyFlagEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
	}

	@Override
	public void startEffect(final Effect effect) {
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}