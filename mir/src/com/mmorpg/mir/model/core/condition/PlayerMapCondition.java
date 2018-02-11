package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerMapCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			 player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (player.getMapId() == value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.PLAYER_NOT_IN_POSITION);
	}

}
