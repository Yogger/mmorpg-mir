package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class HealHotEffect extends EffectTemplate {
	protected int checktime;
	private int[] values;

	private int[] percents;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
		this.checktime = resource.getChecktime();
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
		long damage = MathUtil.calculateHeal(effect.getSkillLevel(), effect.getSkillDamageType(), effect,
				values == null || values.length == 0 ? 0 : values[effect.getSkillLevel() - 1], percents == null
						|| percents.length == 0 ? 0 : percents[effect.getSkillLevel() - 1]);
		effect.setHealHp(damage);
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.HOT.getEffectId());
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		effect.getEffected().getLifeStats().increaseHp(effect.getHealHp());
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.HOT.getEffectId());

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

	public int[] getPercents() {
		return percents;
	}

	public void setPercents(int[] percents) {
		this.percents = percents;
	}

}