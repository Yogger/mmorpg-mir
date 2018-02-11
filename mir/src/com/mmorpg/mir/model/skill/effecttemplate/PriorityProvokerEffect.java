package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Arrays;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.ProvokeTarget;
import com.mmorpg.mir.model.skill.model.ProvokeType;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.windforce.common.utility.RandomUtils;

public class PriorityProvokerEffect extends EffectTemplate {

	protected int[] priorityProbs;
	protected ProvokeType provokeType;
	private ProvokeTarget[] provokeTargets;
	protected int[] skillIds;
	private long[] provokCD;
	/** 被单位类型攻击时触发 */
	private ObjectType[] attackedByType;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		priorityProbs = resource.getPriorityProbs();
		provokeTargets = resource.getProvokeTargets();
		provokeType = resource.getProvokeType();
		skillIds = resource.getProvokeSkills();
		provokCD = resource.getProvokeCD();
		attackedByType = resource.getAttackedByType();
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
	public void startEffect(final Effect effect) {
		ActionObserver observer = null;
		final Creature effector = effect.getEffected();
		switch (provokeType) {
		case ATTACK:
			observer = new ActionObserver(ObserverType.ATTACK) {

				@Override
				public void attack(Creature creature) {
					int skillId = selectProvokeSkill(effect);
					if (skillId != 0) {
						Creature target = getProvokeTarget(indexOfSkillId(skillId), effector, creature);
						createProvokedEffect(effector, target, skillId);
						effect.setLastProvokerEffectTime(System.currentTimeMillis());
					}
				}

			};
			break;
		case SKILLUSE:
			observer = new ActionObserver(ObserverType.SKILLUSE) {
				public void skilluse(Skill skill) {
					int skillId = selectProvokeSkill(effect);
					if (skillId != 0) {
						Creature target = getProvokeTarget(indexOfSkillId(skillId), effector, null);
						createProvokedEffect(effector, target, skillId);
						effect.setLastProvokerEffectTime(System.currentTimeMillis());
					}
				};

			};
			break;
		case ATTACKED:
			observer = new ActionObserver(ObserverType.ATTACKED) {

				@Override
				public void attacked(Creature creature) {
					boolean rightType = false;
					if (attackedByType != null) {
						for (ObjectType ot : attackedByType) {
							if (creature.getObjectType() == ot) {
								rightType = true;
								break;
							}
						}
					}
					if (!rightType) {
						return;
					}
					int skillId = selectProvokeSkill(effect);
					if (skillId != 0) {
						Creature target = getProvokeTarget(indexOfSkillId(skillId), effector, creature);
						createProvokedEffect(effector, target, skillId);
						effect.setLastProvokerEffectTime(System.currentTimeMillis());
					}
				}
			};
			break;
		}

		if (observer == null)
			return;
		effect.getEffected().getObserveController().addObserver(observer);
		effect.setActionObserver(observer);
	}

	private ProvokeTarget indexOfSkillId(int skillId) {
		for (int i = 0; i < skillIds.length; i++) {
			if (skillIds[i] == skillId) {
				return provokeTargets[i];
			}
		}
		return provokeTargets[0];
	}

	private int selectProvokeSkill(Effect effect) {
		int probIndex = 0;
		for (int skillId : skillIds) {
			long cd = (provokCD == null ? 0l : this.provokCD[probIndex]);
			if (cd != 0) {
				if (effect.getEffector().isProvokeCD(skillId, cd)) {
					continue;
				}
			}
			if (RandomUtils.isHit(priorityProbs[probIndex] * 1.0 / 10000)) {
				return skillId;
			}
			probIndex++;
		}

		return 0;
	}

	/**
	 * 
	 * @param effector
	 * @param target
	 */
	private void createProvokedEffect(final Creature effector, Creature target, int skillId) {
		Skill skill = SkillEngine.getInstance().getSkill(effector, skillId, target.getObjectId(),
				target.getPosition() != null ? target.getX() : 0, target.getPosition() != null ? target.getY() : 0,
				target, Arrays.asList(target));
		skill.useProvokeSkill();
	}

	/**
	 * 
	 * @param provokeTarget
	 * @param effector
	 * @param target
	 * @return
	 */
	private Creature getProvokeTarget(ProvokeTarget provokeTarget, Creature effector, Creature target) {
		switch (provokeTarget) {
		case ME:
			return effector;
		case OPPONENT:
			return target;
		}
		throw new IllegalArgumentException("Provoker target is invalid " + provokeTarget);
	}

	@Override
	public void endEffect(Effect effect) {
		ActionObserver observer = effect.getActionObserver();
		if (observer != null)
			effect.getEffected().getObserveController().removeObserver(observer);
	}

	public long[] getProvokCD() {
		return provokCD;
	}

	public void setProvokCD(long[] provokCD) {
		this.provokCD = provokCD;
	}

	public ProvokeTarget[] getProvokeTargets() {
		return provokeTargets;
	}

	public void setProvokeTargets(ProvokeTarget[] provokeTargets) {
		this.provokeTargets = provokeTargets;
	}

	public ObjectType[] getAttackedByType() {
		return attackedByType;
	}

	public void setAttackedByType(ObjectType[] attackedByType) {
		this.attackedByType = attackedByType;
	}
}