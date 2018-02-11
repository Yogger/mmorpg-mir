package com.mmorpg.mir.model.skill.target.chooser;

import java.util.List;

import org.apache.log4j.Logger;
import org.h2.util.New;
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
public class ClientRangeTargetChooser extends AbstractTargetChooser {

	private static final Logger logger = Logger.getLogger(ClientRangeTargetChooser.class);

	@Override
	public TargetType getTargtType() {
		return TargetType.CLIENTRANGE;
	}

	@Override
	public boolean endChooser(Skill skill) {
		return startChooser(skill);
	}

	@Override
	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction) {
		Creature target = null;
		if (targetList == null || targetList.length == 0) {
			return;
		}
		List<Creature> creatureList = New.arrayList();
		for (long tId : targetList) {
			VisibleObject object = World.getInstance().findObject(player.getMapId(), player.getInstanceId(), tId);
			if (object != null) {
				target = (Creature) object;
				creatureList.add(target);
			}
		}
		if (creatureList.isEmpty()) {
			return;
		}
		player.getController().useSkill(skillId, targetId, x, y, target, creatureList);

	}

	@Override
	public boolean startChooser(Skill skill) {
		removeDie(skill.getEffectedList());
		if (skill.getMaxTarget() < skill.getEffectedList().size() || skill.getEffectedList().size() == 0) {
			return false;
		}
		for (Creature creature : skill.getEffectedList()) {
			if (!skill.getEffector().isEnemy(creature)) {
				return false;
			}
			if (!MathUtil.isInRange(creature, skill.getEffector(), skill.getRange(), skill.getRange())) {
				// 超过射程
				logger.warn(String.format("player : [%s] skill : [%s] is out of range", skill.getEffector().getName(),
						skill.getSkillTemplate().getSkillId()));
				if (skill.getEffector() instanceof Player) {
					PacketSendUtility.sendErrorMessage((Player) skill.getEffector(), ManagedErrorCode.OBJECT_TOO_LONG);
				}
				return false;
			}
		}
		return true;
	}
}
