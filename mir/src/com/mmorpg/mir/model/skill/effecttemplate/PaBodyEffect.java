package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;

/**
 * 霸体
 * 
 * @author Kuang Hao
 * @since v1.0 2015-3-21
 * 
 */
public class PaBodyEffect extends EffectTemplate {

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
		effected.getEffectController().unsetAbnormal(EffectId.PABODY.getEffectId(), true);
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.STUN.getEffectId());
		effected.getEffectController().unsetAbnormal(EffectId.CANNOT_MOVE.getEffectId());
		for (Effect abnormalEffect : effected.getEffectController().getAbnormalEffects()) {
			for (EffectTemplate effectTemplate : abnormalEffect.getEffectTemplates()) {
				if (effectTemplate instanceof SlowEffect) {
					effectTemplate.endEffect(abnormalEffect);
				}
			}
		}
		effected.getEffectController().setAbnormal(EffectId.PABODY.getEffectId(), true);
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}