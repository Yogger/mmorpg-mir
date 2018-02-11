package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 技能等级
 */
public class SkillLevelCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}else if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if(player == null){
			this.errorObject(object);
		}
		if (!player.getSkillList().isContainSkill(Integer.valueOf(code))) {
			throw new ManagedException(ManagedErrorCode.SKILL_NOT_LEVEL);
		}
		if (player.getSkillList().getSkills().get(Integer.valueOf(code)).getLevel() >= value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.SKILL_NOT_LEVEL);
	}
}
