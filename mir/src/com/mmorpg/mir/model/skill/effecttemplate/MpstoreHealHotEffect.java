package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class MpstoreHealHotEffect extends EffectTemplate {
	protected int checktime;
	private int[] values;
	private int hpMpStore;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		this.checktime = resource.getChecktime();
		this.hpMpStore = resource.getHpMpStore();
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
		effect.setReserved2(values[0]);
		effect.setReserved3(hpMpStore);
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.MPRESTORE.getEffectId());
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		if (!effect.getEffected().getLifeStats().isFullyRestoredMp()
				&& !effect.getEffected().getLifeStats().isAlreadyDead()) {
			long increase = effect.getEffected().getLifeStats().getMaxMp()
					- effect.getEffected().getLifeStats().getCurrentMp();
			increase = Math.min(Math.min(effect.getReserved2(), effect.getReserved3()), increase);
			effect.getEffected().getLifeStats().increaseMp(increase);
			effect.setReserved3(effect.getReserved3() - increase);
			if (effect.getReserved3() <= 0) {
				// 血包用完自己关自己
				effect.endEffect();
			}
		}
	}

	@Override
	public void replace(Effect oldEffect, Effect newEffect) {
		if (newEffect.getReserved2() == oldEffect.getReserved2()) {
			newEffect.setReserved3(newEffect.getReserved3() + oldEffect.getReserved3());
		} else {
			newEffect.setReserved3(oldEffect.getReserved3());
		}
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.MPRESTORE.getEffectId());

		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {
			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, checktime, checktime);
		effect.addEffectTask(task);
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public int getHpMpStore() {
		return hpMpStore;
	}

	public void setHpMpStore(int hpMpStore) {
		this.hpMpStore = hpMpStore;
	}

}