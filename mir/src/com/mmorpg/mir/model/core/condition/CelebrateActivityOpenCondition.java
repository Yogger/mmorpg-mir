package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.openactive.model.ActivityInfo;
import com.mmorpg.mir.model.serverstate.ServerState;

public class CelebrateActivityOpenCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		ActivityInfo activityInfo = ServerState.getInstance().getCelebrateActivityInfos()
				.get(Integer.parseInt(this.code));
		if (activityInfo == null || !activityInfo.isOpenning()) {
			throw new ManagedException(ManagedErrorCode.NOT_IN_ACTIVITY_PERIOD);
		}
		return true;
	}
}
