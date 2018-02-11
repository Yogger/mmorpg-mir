package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Iterator;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class AuraEffect extends EffectTemplate {

	protected int useSkillId;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		useSkillId = resource.getUseSkillId();
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
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
				if (effector.isEnemy(creature)) {
					if (!creature.getLifeStats().isAlreadyDead()) {
						if (MathUtil.isInRange(effector, creature, effect.getRange(), effect.getRange())) {
							applyAuraTo(effector, creature);
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

	/**
	 * 
	 * @param effector
	 */
	private void applyAuraTo(Creature effector, Creature effected) {
		Skill skill = SkillEngine.getInstance().getSkill(effector, useSkillId, effected.getObjectId(), -1, -1,
				effected, null);
		skill.useSkill();
	}

	@Override
	public void startEffect(final Effect effect) {
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {
			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, 0, 10000);
		effect.addEffectTask(task);
	}

}