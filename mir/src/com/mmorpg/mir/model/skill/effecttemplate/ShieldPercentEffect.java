package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;

public class ShieldPercentEffect extends EffectTemplate {

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
		double result = effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXHP)
				* ((percents[effect.getSkillLevel() - 1] * 1.0) / 10000);
		long percenValue = (long) Math.ceil(result);
		effect.setReserved3(percenValue);
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.SHIELD.getEffectId());
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.SHIELD.getEffectId());
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