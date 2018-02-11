package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class CountryStatBuffEffect extends EffectTemplate {

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
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
		if (effect.getEffected() instanceof Player) {
			((Player) effect.getEffected()).clearUnityBuff();
		}
	}

	@Override
	public void startEffect(final Effect effect) {
		if (effect.getEffected() instanceof Player) {
			((Player) effect.getEffected()).addUnityBuff(true);
		}
	}

	public static void main(String[] args) {
		Stat[] ss = new Stat[] {};
		System.out.println(ss.getClass());
		System.out.println(Stat.class);
	}
}