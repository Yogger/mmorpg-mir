package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.skill.model.Skill;

public class SkillDpCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Skill skill = null;
		if(object instanceof Skill){
			skill = (Skill) object;
		}
		if(skill == null){
			this.errorObject(object);
		}
		PlayerLifeStats playerLifeStats = (PlayerLifeStats) skill.getEffector().getLifeStats();
		if (playerLifeStats.getCurrentDp() >= value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_DP);
	}

}
