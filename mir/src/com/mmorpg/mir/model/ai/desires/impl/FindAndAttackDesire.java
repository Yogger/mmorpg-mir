package com.mmorpg.mir.model.ai.desires.impl;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.AIUtil;
import com.mmorpg.mir.model.ai.desires.AbstractDesire;
import com.mmorpg.mir.model.ai.desires.impl.AttackDesire.UseState;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;

public final class FindAndAttackDesire extends AbstractDesire {

	private BigBrother owner;
	private String questId;

	public FindAndAttackDesire(BigBrother npc, String questId, int desirePower) {
		super(desirePower);
		this.owner = npc;
		this.questId = questId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleDesire(AI ai) {

		Player player = owner.getMaster();
		if (player.getQuestPool().getCompletionHistory().containsKey(questId)) {
			return false;
		}

		if (!AIUtil.addBigBrotherHeat(owner, questId)) {
			return true;
		}

		// owner.unRide();

		Creature target = owner.getAggroList().getMostHated();
		if (target == null) {
			return true;
		}
		owner.setTarget(target);

		int fx = owner.getX();
		int fy = owner.getY();
		int tx = target.getX();
		int ty = target.getY();

		// 这里需要有使用技能的判断

		UseState useState = owner.getSkillSelector().selectAndUseSkill(target);
		if (useState == UseState.OK) {
			return true;
		}

		if (!owner.canMove()) {
			return true;
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
			return true;
		}

		owner.getMoveController().setFollowTarget(true);
		owner.getMoveController().setNewRoads(fx, fy, road);
		owner.getMoveController().schedule();

		return true;
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