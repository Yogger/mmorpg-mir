package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.Arrays;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.ProvokeTarget;
import com.mmorpg.mir.model.skill.model.ProvokeType;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.windforce.common.utility.RandomUtils;

public class ProvokerEffect extends EffectTemplate {

	protected int prob;
	protected ProvokeTarget provokeTarget;
	protected ProvokeType provokeType;
	protected int[] skillIds;
	private long[] provokCD;
	/** 被单位类型攻击时触发 */
	private ObjectType[] attackedByType;
	/** 触发技能,施法者需要满足的条件 */
	private String[] provokeEffectorConditionIds;

	private CoreConditions provokeEffectorConditions;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		prob = resource.getProvokeProb();
		provokeTarget = resource.getProvokeTarget();
		provokeType = resource.getProvokeType();
		skillIds = resource.getProvokeSkills();
		provokCD = resource.getProvokeCD();
		attackedByType = resource.getAttackedByType();
		provokeEffectorConditionIds = resource.getProvokeEffectorConditionIds();

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
					long provokCDS = 0;
					if (provokCD != null) {
						provokCDS = provokCD[0];
					}
					if (isProvoke(effect, prob, provokCDS, effector)) {
						Creature target = getProvokeTarget(provokeTarget, effector, creature);
						createProvokedEffect(effector, target);
						effect.setLastProvokerEffectTime(System.currentTimeMillis());
					}
				}

			};
			break;
		case SKILLUSE:
			observer = new ActionObserver(ObserverType.SKILLUSE) {
				public void skilluse(Skill skill) {
					throw new RuntimeException("没有目标触发!");
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
					long provokCDS = 0;
					if (provokCD != null) {
						provokCDS = provokCD[0];
					}
					if (isProvoke(effect, prob, provokCDS, effector)) {
						Creature target = getProvokeTarget(provokeTarget, effector, creature);
						createProvokedEffect(effector, target);
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

	private boolean isProvoke(Effect effect, int prob, long provokeCD, Creature effector) {
		if (provokeEffectorConditionIds != null && provokeEffectorConditions == null) {
			CoreConditionResource[] resources = CoreConditionManager.getInstance().getCoreConditionResources(
					provokeEffectorConditionIds);
			provokeEffectorConditions = CoreConditionManager.getInstance().getCoreConditions(1, resources);
		}
		if (provokeEffectorConditions != null) {
			if (!provokeEffectorConditions.verify(effector, false)) {
				return false;
			}
		}
		if (provokeCD != 0) {
			if ((System.currentTimeMillis() - effect.getLastProvokerEffectTime()) < provokeCD) {
				return false;
			}
		}

		return RandomUtils.isHit(prob * 1.0 / 10000);
	}

	/**
	 * 
	 * @param effector
	 * @param target
	 */
	private void createProvokedEffect(final Creature effector, Creature target) {
		for (int skillId : skillIds) {
			Skill skill = SkillEngine.getInstance().getSkill(effector, skillId, target.getObjectId(),
					target.getPosition() != null ? target.getX() : 0, target.getPosition() != null ? target.getY() : 0,
					target, Arrays.asList(target));
			skill.useProvokeSkill();
		}
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

	public ObjectType[] getAttackedByType() {
		return attackedByType;
	}

	public void setAttackedByType(ObjectType[] attackedByType) {
		this.attackedByType = attackedByType;
	}

	public String[] getProvokeEffectorConditionIds() {
		return provokeEffectorConditionIds;
	}

	public void setProvokeEffectorConditionIds(String[] provokeEffectorConditionIds) {
		this.provokeEffectorConditionIds = provokeEffectorConditionIds;
	}

}