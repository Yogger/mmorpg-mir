package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.packet.SM_PeriodicAction;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class BleedPercentEffect extends EffectTemplate {
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
		if (effect.getEffected().getEffectController().isAbnoramlSet(EffectId.GOD)) {
			effect.setReserved2(0);
		} else {
			long damage = effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXHP);
			int v = 0;
			if (values != null && values.length >= effect.getSkillLevel()) {
				v = values[effect.getSkillLevel() - 1];
			}
			damage = (long) (Math.ceil((damage * 1.0 / 10000) * percents[effect.getSkillLevel() - 1]) + v);
			effect.setReserved2(damage);
		}
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.BLEED.getEffectId(), true);
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		Creature effected = effect.getEffected();
		Creature effector = effect.getEffector();

		if (!effect.canAttackType(effect.getEffected())) {
			return;
		}
		DamageResult damageResult = DamageResult.valueOf();
		effected.getController().onAttack(effector, effect.getSkillId(), effect.getReserved2(), damageResult);
		SM_PeriodicAction sm = null;
		if (damageResult.getDamage() != 0) {
			sm = SM_PeriodicAction.valueOf(effect.getEffector().getObjectId(), effect.getSkillId(),
					damageResult.getDamage(), effect.getEffected().getObjectId(), null);
		} else {
			sm = SM_PeriodicAction.valueOf(effect.getEffector().getObjectId(), effect.getSkillId(),
					effect.getReserved2(), effect.getEffected().getObjectId(), null);
		}
		PacketSendUtility.sendPacket(effect.getEffected(), sm);
		PacketSendUtility.sendPacket(effect.getEffector(), sm);
		// if (effect.getBloodSuckValue() > 0) {
		// effect.getEffector().getLifeStats().increaseHp(effect.getBloodSuckValue());
		// }
		// if (effect.getReboundValue() > 0) {
		// effect.getEffector().getController().onAttack(effect.getEffected(),
		// effect.getReboundValue());
		// }
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		effected.getEffectController().setAbnormal(EffectId.BLEED.getEffectId(), true);

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