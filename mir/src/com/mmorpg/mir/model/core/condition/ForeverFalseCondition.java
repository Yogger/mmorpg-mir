package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;

public class ForeverFalseCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		return false;
	}

	@Override
	public void init(CoreConditionResource resource) {
	}

}
