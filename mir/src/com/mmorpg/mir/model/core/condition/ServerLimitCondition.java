package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.serverstate.ServerState;

public class ServerLimitCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		if (ServerState.getInstance().serverLimitVerify(code)) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.BEYOND_SERVER_LIMIT);
	}

}
