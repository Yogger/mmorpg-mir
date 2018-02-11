package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.manager.SkillManager;

/**
 * 玩家奖励ID条件
 */
public class SkillRewardIdCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if(player == null){
			this.errorObject(object);
		}
		player.getSkillList().addRewardSkill(SkillManager.getInstance().getResource(Integer.valueOf(code)));

		throw new ManagedException(ManagedErrorCode.SKILL_NOT_INREWARD);
	}
}
