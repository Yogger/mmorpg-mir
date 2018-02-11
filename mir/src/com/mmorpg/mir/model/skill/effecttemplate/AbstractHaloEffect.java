package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.concurrent.Future;

import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public abstract class AbstractHaloEffect extends EffectTemplate {

	protected int checktime;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		checktime = resource.getChecktime();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(final Effect effect) {
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {
			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, 0, checktime);
		effect.addEffectTask(task);
	}

}
