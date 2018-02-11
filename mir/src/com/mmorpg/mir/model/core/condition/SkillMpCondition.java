package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.skill.model.Skill;

public class SkillMpCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Skill skill = null;
		if(object instanceof Skill){
			skill =(Skill) object;
		}
		if(skill == null){
			this.errorObject(object);
		}
		if (skill.getEffector().getLifeStats().getCurrentMp() >= value) {
			return true;
		}
		// throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_MP);
		return false;
	}

}
