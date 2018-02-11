package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;

public class ForeverTrueCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		return true;
	}

	@Override
	public void init(CoreConditionResource resource) {
	}

}
