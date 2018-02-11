package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Iterator;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.DamageType;
import com.mmorpg.mir.model.skill.packet.SM_PeriodicAction;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.SelectRandom;

public class InvincibleCutDamageEffectTemplate extends AbstractHaloEffect {

	private int[] values;

	private int[] percents;

	private final static int MAX_DAMAGE_COUNT = 7;

	private final static String VALUE_KEY = "INVINCIBLECUTDAMAGEEFFECTTEMPLATE";

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		int count = effect.getValues().get(VALUE_KEY);
		if (count >= MAX_DAMAGE_COUNT) {
			effect.endEffect();
			return;
		}
		Creature effector = effect.getEffector();
		Iterator<VisibleObject> ite = effector.getKnownList().iterator();
		SelectRandom<Creature> selector = new SelectRandom<Creature>();
		while (ite.hasNext()) {
			VisibleObject obj = ite.next();
			if (obj instanceof Creature) {
				Creature creature = (Creature) obj;
				if (effector.isEnemy(creature) && effect.canAttackType(creature)
						&& MathUtil.isInRange(effector, effector, effect.getRange(), effect.getRange())) {
					if (!creature.getLifeStats().isAlreadyDead()) {
						selector.addElement(creature, 1);
					}
				}
			}
		}
		if (selector.size() != 0) {
			Creature effected = selector.run();
			long damage = MathUtil.calculateDamage(effect.getSkillLevel(), effect.getSkillDamageType(), effector,
					effected, values[effect.getSkillLevel() - 1], percents[effect.getSkillLevel() - 1]);
			long rate = MathUtil.calDamageRate(effect);
			damage = this.selectDamageType(DamageType.valueOf(effect.getDamageType())).calcDamage(damage, rate);
			PacketSendUtility.sendPacket(
					(Player) effector,
					SM_PeriodicAction.valueOf(effect.getEffector().getObjectId(), effect.getSkillId(), damage,
							effected.getObjectId(), effect.getSpecialEffect()));
			DamageResult damageResult = DamageResult.valueOf();
			effected.getController().onAttack(effector, effect.getSkillId(), damage, damageResult);
			effect.getValues().put(VALUE_KEY, count + 1);
		} else {
			// 没有目标自动停止
			endEffect(effect);
		}
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffector();
		effected.getEffectController().unsetAbnormal(EffectId.INVINCIBLECUT.getEffectId(), true);
	}

	@Override
	public void startEffect(Effect effect) {
		Creature effected = effect.getEffector();
		effected.getEffectController().setAbnormal(EffectId.INVINCIBLECUT.getEffectId(), true);
		effect.getValues().put(VALUE_KEY, 0);
		super.startEffect(effect);
	}

}
