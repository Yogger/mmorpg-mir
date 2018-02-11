package com.mmorpg.mir.model.skill.target.chooser;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class EnemyXYRangeTargetChooser extends AbstractTargetChooser {

	@Override
	public TargetType getTargtType() {
		return TargetType.ENEMYXYRANGE;
	}

	@Override
	public boolean endChooser(Skill skill) {
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
		if (x >= 0 && y >= 0) {
			player.getController().useSkill(skillId, targetId, x, y, null, null);
		}
	}

	@Override
	public boolean startChooser(Skill skill) {
		if (!MathUtil.isInRange(skill.getX(), skill.getY(), skill.getEffector(), skill.getRange(), skill.getRange())) {
			if (skill.getEffector() instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) skill.getEffector(), ManagedErrorCode.OBJECT_TOO_LONG);
			}
			// 超过射程
			return false;
		}
		return true;
	}

}
