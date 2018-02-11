package com.mmorpg.mir.model.skill.target.chooser;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;

@Component
public class RangeTargetChooser extends AbstractTargetChooser {

	@Override
	public TargetType getTargtType() {
		return TargetType.RANGE;
	}

	@Override
	public boolean endChooser(Skill skill) {
		if (!startChooser(skill)) {
			return false;
		}
		skill.setX(skill.getFirstTarget().getX());
		skill.setY(skill.getFirstTarget().getY());

		return xyChooser(skill);
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
		if (skill.getFirstTarget().getLifeStats().isAlreadyDead()) {
			return false;
		}
		if (!MathUtil.isInRange(skill.getEffector(), skill.getFirstTarget(), skill.getRange(), skill.getRange())) {
			if (skill.getEffector() instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) skill.getEffector(), ManagedErrorCode.OBJECT_TOO_LONG);
			}
			// 超过射程
			return false;
		}
		return true;
	}

}
