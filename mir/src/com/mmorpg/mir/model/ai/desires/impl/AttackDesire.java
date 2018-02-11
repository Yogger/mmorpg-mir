package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;

public final class AttackDesire extends AbstractDesire {

	public enum UseState {
		OK, WAIT, CHANGE, FAIL;
	}

	protected Npc owner;

	protected Creature target;

	private int attackCounter;

	public AttackDesire(Npc npc, Creature target, int desirePower) {
		super(desirePower);
		this.target = target;
		this.owner = npc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleDesire(AI ai) {

		if (target == null || target.getLifeStats().isAlreadyDead()) {
			// TODO lower hate and not reset
			owner.getAggroList().stopHating(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}
		int mapId = owner.getMapId();
		int targetMapId = target.getMapId();
		if (mapId != targetMapId || owner.getInstanceId() != target.getInstanceId()) {
			owner.getAggroList().stopHating(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}

		int fx = owner.getX();
		int fy = owner.getY();
		int tx = target.getX();
		int ty = target.getY();

		if (owner.tooFarFromHome(tx, ty)) {
			owner.getMoveController().stopMoving();
			owner.getAggroList().stopHating(target);
			owner.getAi().handleEvent(Event.FAR_FROM_HOME);
			return false;
		}

		// 这里需要有使用技能的判断
		UseState useState = owner.getSkillSelector().selectAndUseSkill(target);
		if (useState == UseState.OK) {
			if (++attackCounter % 3 == 0) {
				if (!owner.getAggroList().isMostHated(target)) {
					owner.getAi().handleEvent(Event.MOST_HATED_CHANGED);
					return false;
				}
			}
			return true;
		}

		if (!owner.canMove()) {
			owner.getMoveController().stopMoving();
			owner.getAggroList().discountHating(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}

		if (!owner.canPerformMove()) {
			return true;
		}

		if (!owner.getMoveController().isStopped()) {
			return true;
		}

		int dis = MathUtil.getGridDistance(fx, fy, tx, ty);
		if (dis <= owner.getAtkRange()) {
			return true;
		}

		WorldMap map = World.getInstance().getWorldMap(owner.getMapId());
		int[] point = map.createXY(tx, ty, 2);

		Road road = MathUtil.SmoothFindRoad(owner.getMapId(), fx, fy, point[0], point[1]);

		if (road == null) {
			road = MathUtil.findRoad(owner.getMapId(), fx, fy, point[0], point[1]);
		}

		if (road == null) {
			owner.getAggroList().stopHating(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}

		owner.getMoveController().setFollowTarget(true);
		owner.getMoveController().setNewRoads(fx, fy, road);
		owner.getMoveController().schedule();

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AttackDesire))
			return false;

		AttackDesire that = (AttackDesire) o;

		return target.equals(that.target);
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}

	public Creature getTarget() {
		return target;
	}

	@Override
	public int getExecutionInterval() {
		return 1;
	}

	@Override
	public void onClear() {
		owner.getMoveController().stop();
	}

	public boolean canUseSkill(Skill skill) {
		if (owner.isPublicDisabled(skill.getSkillTemplate().getPublicCoolDownGroup())) {
			return false;
		}
		if (owner.isSkillDisabled(skill.getSkillTemplate().getSkillId())) {
			return false;
		}
		if (owner.getEffectController().isAbnoramlSet(EffectId.STUN)) {
			return false;
		}
		if (owner.getEffectController().isAbnoramlSet(EffectId.SILENCE)) {
			return false;
		}
		return true;
	}

}