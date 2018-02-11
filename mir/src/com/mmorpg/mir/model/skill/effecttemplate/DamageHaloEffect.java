package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Iterator;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.DamageType;
import com.mmorpg.mir.model.skill.packet.SM_PeriodicAction;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class DamageHaloEffect extends AbstractHaloEffect {

	private int[] values;

	private int[] percents;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		percents = resource.getPercents();
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		Creature effector = effect.getEffector();
		Iterator<VisibleObject> ite = effector.getKnownList().iterator();
		int count = 0;
		while (ite.hasNext()) {
			VisibleObject obj = ite.next();
			if (obj instanceof Creature) {
				Creature creature = (Creature) obj;
				if (effector.isEnemy(creature) && effect.canAttackType(creature)) {
					if (!creature.getLifeStats().isAlreadyDead()) {
						long damage = MathUtil.calculateDamage(effect.getSkillLevel(), effect.getSkillDamageType(),
								effector, creature, values[effect.getSkillLevel() - 1],
								percents[effect.getSkillLevel() - 1]);
						long targetDamageReduce = effect.getEffected().getGameStats()
								.getCurrentStat(StatEnum.CRITICAL_DAMAGE_REDUCE);
						long rate = MathUtil.calDamageRate(effect);
						damage = this.selectDamageType(DamageType.valueOf(effect.getDamageType())).calcDamage(damage,
								rate);
						PacketSendUtility.sendPacket((Player) effector, SM_PeriodicAction.valueOf(effect.getEffector()
								.getObjectId(), effect.getSkillId(), damage, creature.getObjectId(), null));
						DamageResult damageResult = DamageResult.valueOf();
						creature.getController().onAttack(effector, effect.getSkillId(), damage, damageResult);
						count++;
						if (count >= effect.maxSkillTarget()) {
							break;
						}
					}
				}
			}
		}
	}
}
