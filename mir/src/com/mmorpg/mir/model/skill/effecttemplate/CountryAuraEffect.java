package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Iterator;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class CountryAuraEffect extends AuraEffect {

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
		Creature effector = effect.getEffector();
		Iterator<VisibleObject> ite = effector.getKnownList().iterator();
		while (ite.hasNext()) {
			VisibleObject obj = ite.next();
			if (obj instanceof Player) {
				Creature creature = (Creature) obj;
				if (effector.isEnemy(creature)) {
					if (!creature.getLifeStats().isAlreadyDead()) {
						applyAuraTo(effector, creature);
					}
				}
			}
		}
	}

	public static void doApplyAuraTo(Creature effector, Creature effected, int useSkillId) {
		Player player = (Player) effected;
		if (player.getCountry().getBuffFloor() <= 0) {
			return;
		}
		Skill skill = SkillEngine.getInstance().getSkill(null, useSkillId, effected.getObjectId(), -1, -1, effected,
				null);
		skill.noEffectorUseSkill();

	}

	/**
	 * 
	 * @param effector
	 */
	private void applyAuraTo(Creature effector, Creature effected) {
		doApplyAuraTo(effector, effected, useSkillId);
	}

	@Override
	public void startEffect(final Effect effect) {
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {
			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, 0, 8000);
		effect.addEffectTask(task);
	}

}