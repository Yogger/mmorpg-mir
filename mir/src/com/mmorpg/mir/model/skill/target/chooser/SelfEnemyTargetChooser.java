package com.mmorpg.mir.model.skill.target.chooser;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.world.World;

@Component
public class SelfEnemyTargetChooser extends AbstractTargetChooser {

	@Override
	public TargetType getTargtType() {
		return TargetType.SELF_RANGE_ENEMY;
	}

	@Override
	public boolean endChooser(Skill skill) {
		if (!startChooser(skill)) {
			return false;
		}
		skill.setX(skill.getFirstTarget().getX());
		skill.setY(skill.getFirstTarget().getY());

		xyChooser(skill);
		// skill.getEffectedList().add(skill.getEffector());
		return true;
	}

	@Override
	protected boolean xyTargetSelect(Skill skill, Creature target) {
		if (skill.getEffector().isEnemy(target) && !target.getLifeStats().isAlreadyDead()) {
			return true;
		}
		return false;
	}

	@Override
	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction) {
		if (targetId == -1 || targetId == 0) {
			return;
		}
		VisibleObject object = World.getInstance().findObject(player.getMapId(), player.getInstanceId(), targetId);
		if (object == null && !(object instanceof Creature)) {
			return;
		}
		player.getController().useSkill(skillId, targetId, x, y, (Creature) object, null);

	}

	@Override
	public boolean startChooser(Skill skill) {
		if (skill.getEffector().getLifeStats().isAlreadyDead()) {
			return false;
		}
		skill.setFirstTarget(skill.getEffector());
		return true;
	}

}
