package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.serverstate.ServerState;

public class MergeHasDoneCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		return ServerState.getInstance().hasMerged();
	}

}
