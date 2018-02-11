package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerNotDieCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			 player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (!player.getLifeStats().isAlreadyDead()) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
	}

}
