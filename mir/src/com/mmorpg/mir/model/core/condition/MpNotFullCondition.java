package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class MpNotFullCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			 player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (player.getLifeStats().isFullyRestoredMp()) {
			throw new ManagedException(ManagedErrorCode.MP_FULL_ERROR);
		}
		return true;
	}

	@Override
	public void init(CoreConditionResource resource) {
	}

	@Override
	public void calculate(int num) {
	}
}
