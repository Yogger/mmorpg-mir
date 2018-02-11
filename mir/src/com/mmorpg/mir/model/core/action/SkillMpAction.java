package com.mmorpg.mir.model.core.action;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.skill.model.Skill;

public class SkillMpAction extends AbstractCoreAction {

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Skill skill = (Skill) object;
		skill.getEffector().getLifeStats().reduceMp(value);
	}

	@Override
	public boolean verify(Object object) {
		return CoreConditionType.createSkillMpCondition(value).verify(object);
	}

	@Override
	public AbstractCoreAction clone() {
		SkillMpAction countryCoppersAction = new SkillMpAction();
		countryCoppersAction.setCode(code);
		countryCoppersAction.setValue(value);
		return countryCoppersAction;
	}
}
