package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.object.resource.SkillSelectorItemSample;

public class SkillSelectorItem {
	private int skillId;
	private CoreConditions conditions;
	private CoreConditions targetConditions;

	public static SkillSelectorItem valueOf(SkillSelectorItemSample ssi) {
		SkillSelectorItem item = new SkillSelectorItem();
		item.skillId = ssi.getSkillId();
		if (ssi.getUseConditions() != null) {
			item.conditions = CoreConditionManager.getInstance().getCoreConditions(1, ssi.getUseConditions());
		}
		if (ssi.getTargetConditions() != null) {
			item.targetConditions = CoreConditionManager.getInstance().getCoreConditions(1, ssi.getTargetConditions());
		}
		return item;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public CoreConditions getConditions() {
		return conditions;
	}

	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

	public CoreConditions getTargetConditions() {
		return targetConditions;
	}

	public void setTargetConditions(CoreConditions targetConditions) {
		this.targetConditions = targetConditions;
	}

}
