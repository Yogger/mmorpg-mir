package com.mmorpg.mir.model.core.action;

import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.skill.model.Skill;

public class SkillDpAction extends AbstractCoreAction {

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Skill skill = (Skill) object;
		PlayerLifeStats playerLifeStats = (PlayerLifeStats) skill.getEffector().getLifeStats();
		playerLifeStats.reduceDp(value);
	}

	@Override
	public boolean verify(Object object) {
		return CoreConditionType.createSkillDpCondition(value).verify(object);
	}

	@Override
	public AbstractCoreAction clone() {
		SkillDpAction countryCoppersAction = new SkillDpAction();
		countryCoppersAction.setCode(code);
		countryCoppersAction.setValue(value);
		return countryCoppersAction;
	}

}
